/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.ffi.processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;

import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public final class FFIProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add("com.oracle.truffle.r.ffi.processor.RFFIUpCallRoot");
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }
        process0(roundEnv);
        return true;
    }

    private void process0(RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(RFFIUpCallRoot.class)) {
            try {
                processElement(e);
            } catch (Throwable ex) {
                ex.printStackTrace();
                String message = "Uncaught error in " + this.getClass();
                processingEnv.getMessager().printMessage(Kind.ERROR, message + ": " + printException(ex), e);
            }
        }
    }

    private void processElement(Element e) throws IOException {
        if (e.getKind() != ElementKind.INTERFACE) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "RFFIUpCallRoot mjusty annotate an interface");
        }
        Types types = processingEnv.getTypeUtils();
        TypeElement typeElement = (TypeElement) e;
        List<? extends TypeMirror> extended = typeElement.getInterfaces();
        int count = 0;
        for (TypeMirror tm : extended) {
            TypeElement x = (TypeElement) types.asElement(tm);
            List<? extends Element> methods = x.getEnclosedElements();
            count += methods.size();
        }
        ExecutableElement[] methods = new ExecutableElement[count];
        count = 0;
        for (TypeMirror tm : extended) {
            TypeElement x = (TypeElement) types.asElement(tm);
            List<? extends Element> encMethods = x.getEnclosedElements();
            for (Element encMethod : encMethods) {
                methods[count++] = (ExecutableElement) encMethod;
            }
        }
        Arrays.sort(methods, new Comparator<ExecutableElement>() {
            @Override
            public int compare(ExecutableElement e1, ExecutableElement e2) {
                return e1.getSimpleName().toString().compareTo(e2.getSimpleName().toString());
            }
        });
        generateTable(methods);
        generateMessageClasses(methods);
        generateCallbacks(methods);
        generateCallbacksIndexHeader(methods);
    }

    private void generateTable(ExecutableElement[] methods) throws IOException {
        JavaFileObject fileObj = processingEnv.getFiler().createSourceFile("com.oracle.truffle.r.ffi.impl.upcalls.RFFIUpCallTable");
        Writer w = fileObj.openWriter();
        w.append("// GENERATED; DO NOT EDIT\n");
        w.append("package com.oracle.truffle.r.ffi.impl.upcalls;\n");
        w.append("public enum RFFIUpCallTable {\n");
        for (int i = 0; i < methods.length; i++) {
            ExecutableElement method = methods[i];
            w.append("    ").append(method.getSimpleName().toString()).append(i == methods.length - 1 ? ";" : ",").append('\n');
        }

        w.append("}\n");
        w.close();
    }

    private void generateCallbacksIndexHeader(ExecutableElement[] methods) throws IOException {
        FileObject fileObj = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "com.oracle.truffle.r.ffi.impl.upcalls", "rffi_upcallsindex.h");
        note("If you edited any UpCallsRFFI interfaces do: cp " + fileObj.toUri().getPath() + " com.oracle.truffle.r.native/fficall/src/common\n");
        Writer w = fileObj.openWriter();
        w.append("// GENERATED; DO NOT EDIT\n");
        w.append("#ifndef RFFI_UPCALLSINDEX_H\n");
        w.append("#define RFFI_UPCALLSINDEX_H\n");
        w.append('\n');
        for (int i = 0; i < methods.length; i++) {
            ExecutableElement method = methods[i];
            w.append("#define ").append(method.getSimpleName().toString()).append("_x ").append(Integer.toString(i)).append('\n');
        }
        w.append('\n');
        w.append("#define ").append("UPCALLS_TABLE_SIZE ").append(Integer.toString(methods.length)).append('\n');
        w.append('\n');
        w.append("#endif // RFFI_UPCALLSINDEX_H\n");
        w.close();
    }

    private void generateMessageClasses(ExecutableElement[] methods) throws IOException {
        for (int i = 0; i < methods.length; i++) {
            ExecutableElement m = methods[i];
            generateCallClass(m);
            generateMessageClass(m);
        }
    }

    private void generateCallClass(ExecutableElement m) throws IOException {
        String name = m.getSimpleName().toString();
        String callName = name + "Call";
        JavaFileObject fileObj = processingEnv.getFiler().createSourceFile("com.oracle.truffle.r.ffi.impl.upcalls." + callName);
        Writer w = fileObj.openWriter();
        w.append("// GENERATED; DO NOT EDIT\n");
        w.append("package ").append("com.oracle.truffle.r.ffi.impl.upcalls").append(";\n\n");
        w.append("import com.oracle.truffle.api.interop.ForeignAccess;\n");
        w.append("import com.oracle.truffle.api.interop.TruffleObject;\n");
        w.append("import com.oracle.truffle.r.runtime.data.RTruffleObject;\n");
        w.append("// Checkstyle: stop method name check\n\n");
        w.append("public final class ").append(callName).append(" implements RTruffleObject {\n");
        w.append('\n');
        w.append("    public final UpCallsRFFI upCallsImpl;\n");
        w.append('\n');
        w.append("    protected ").append(callName).append("(UpCallsRFFI upCallsImpl) {\n");
        w.append("        this.upCallsImpl = upCallsImpl;\n");
        w.append("    }\n");
        w.append('\n');
        w.append("    public static boolean isInstance(TruffleObject value) {\n");
        w.append("        return value instanceof ").append(callName).append(";\n");
        w.append("    }\n");
        w.append('\n');

        w.append("    @Override\n");
        w.append("    public ForeignAccess getForeignAccess() {\n");
        w.append("        return ").append(callName).append("MRForeign.ACCESS;\n");
        w.append("    }\n");
        w.append('\n');
        w.append("}\n");
        w.close();
    }

    private void generateMessageClass(ExecutableElement m) throws IOException {
        String name = m.getSimpleName().toString();
        String callName = name + "Call";
        String returnType = getTypeName(m.getReturnType());
        List<? extends VariableElement> params = m.getParameters();

        StringBuilder arguments = new StringBuilder();
        boolean usesUnwrap = false;

        int lparams = params.size();
        for (int i = 0; i < lparams; i++) {
            String is = Integer.toString(i);
            String paramTypeName = getTypeName(params.get(i).asType());
            boolean isScalar = true;
            boolean needCast = !paramTypeName.equals("java.lang.Object");
            if (needCast) {
                arguments.append('(').append(paramTypeName).append(") ");
            }
            if (isScalar) {
                usesUnwrap = true;
                arguments.append("unwrap(");
            }
            arguments.append("arguments[").append(is).append("]");
            if (isScalar) {
                arguments.append(')');
            }
            if (i != lparams - 1) {
                arguments.append(", ");
            }
        }

        JavaFileObject fileObj = processingEnv.getFiler().createSourceFile("com.oracle.truffle.r.ffi.impl.upcalls." + callName + "MR");
        Writer w = fileObj.openWriter();
        w.append("// GENERATED; DO NOT EDIT\n");
        w.append("package ").append("com.oracle.truffle.r.ffi.impl.upcalls").append(";\n\n");
        if (usesUnwrap) {
            w.append("import static com.oracle.truffle.r.ffi.impl.nfi.TruffleNFI_Utils.unwrap;\n");
        }
        w.append("import com.oracle.truffle.api.interop.MessageResolution;\n");
        w.append("import com.oracle.truffle.api.interop.Resolve;\n");
        w.append("import com.oracle.truffle.api.nodes.Node;\n");
        w.append("import com.oracle.truffle.r.ffi.impl.interop.NativePointer;\n");
        w.append("// Checkstyle: stop method name check\n\n");

        w.append("@MessageResolution(receiverType = ").append(name).append("Call.class)\n");
        w.append("public class ").append(callName).append("MR {\n");
        w.append("    @Resolve(message = \"EXECUTE\")\n");
        w.append("    public abstract static class ").append(callName).append("Execute extends Node {\n");
        w.append("        protected ").append(returnType).append(" access(").append(callName).append(" receiver, ");
        if (params.size() == 0) {
            w.append("@SuppressWarnings(\"unused\") ");
        }
        w.append("Object[] arguments) {\n");
        w.append("            ").append("return").append(" receiver.upCallsImpl.").append(name).append("(");

        w.append(arguments);

        w.append(");\n");
        w.append("        }\n");
        w.append("    }\n");
        w.append("\n");

        w.append("    @Resolve(message = \"IS_EXECUTABLE\")\n");
        w.append("    public abstract static class ").append(callName).append("IsExecutable extends Node {\n");
        w.append("        protected Object access(@SuppressWarnings(\"unused\") ").append(callName).append(" receiver) {\n");
        w.append("            return true;\n");
        w.append("        }\n");
        w.append("    }\n");
        w.append("\n");

        w.append("}\n");
        w.close();

    }

    private void generateCallbacks(ExecutableElement[] methods) throws IOException {
        JavaFileObject fileObj = processingEnv.getFiler().createSourceFile("com.oracle.truffle.r.ffi.impl.upcalls.Callbacks");
        Writer w = fileObj.openWriter();
        w.append("// GENERATED; DO NOT EDIT\n");
        w.append("package ").append("com.oracle.truffle.r.ffi.impl.upcalls").append(";\n\n");
        w.append("import com.oracle.truffle.api.interop.TruffleObject;\n");
        w.append("import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;\n");
        w.append("import com.oracle.truffle.r.ffi.impl.upcalls.UpCallsRFFI;\n\n");
        w.append("public enum Callbacks {\n");
        for (int i = 0; i < methods.length; i++) {
            ExecutableElement m = methods[i];
            String sig = getNFISignature(m);
            w.append("    ").append(m.getSimpleName().toString()).append('(').append('"').append(sig).append('"').append(')');
            w.append(i == methods.length - 1 ? ';' : ',');
            w.append("\n");
        }
        w.append('\n');
        w.append("    public final String nfiSignature;\n");
        w.append("    @CompilationFinal public TruffleObject call;\n\n");
        w.append("    Callbacks(String signature) {\n");
        w.append("        this.nfiSignature = signature;\n");
        w.append("    }\n\n");

        w.append("    public static void createCalls(UpCallsRFFI upCallsRFFIImpl) {\n");
        w.append("        for (Callbacks callback : values()) {\n");
        w.append("            switch (callback) {\n");
        for (int i = 0; i < methods.length; i++) {
            ExecutableElement m = methods[i];
            String callName = m.getSimpleName().toString() + "Call";
            w.append("                case ").append(m.getSimpleName().toString()).append(":\n");
            w.append("                    callback.call = new ").append(callName).append("(upCallsRFFIImpl);\n");
            w.append("                    break;\n\n");
        }
        w.append("            }\n");
        w.append("        }\n");
        w.append("    }\n");
        w.append("}\n");
        w.close();
    }

    private String getNFISignature(ExecutableElement m) {
        List<? extends VariableElement> params = m.getParameters();
        int lparams = params.size();
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i < lparams; i++) {
            VariableElement param = params.get(i);
            RFFICstring[] annotations = param.getAnnotationsByType(RFFICstring.class);
            String nfiParam = nfiParamName(getTypeName(param.asType()), annotations.length == 0 ? null : annotations[0]);
            sb.append(nfiParam);
            if (i != lparams - 1) {
                sb.append(", ");
            }
        }
        sb.append(')');
        sb.append(" : ");
        sb.append(nfiParamName(getTypeName(m.getReturnType()), null));
        return sb.toString();
    }

    private static String nfiParamName(String paramType, RFFICstring rffiCstring) {
        switch (paramType) {
            case "java.lang.Object":
                if (rffiCstring == null) {
                    return "object";
                } else {
                    return rffiCstring.convert() ? "string" : "pointer";
                }
            case "char":
                return "uint8";
            case "int":
                return "sint32";
            case "double":
                return "double";
            case "void":
                return "void";
            case "int[]":
                return "[sint32]";
            case "double[]":
                return "[double]";
            case "byte[]":
                return "[uint8]";
            default:
                return "object";
        }

    }

    private String getTypeName(TypeMirror type) {
        Types types = processingEnv.getTypeUtils();
        TypeKind kind = type.getKind();
        String returnType;
        if (kind.isPrimitive()) {
            returnType = kind.name().toLowerCase();
        } else {
            Element rt = types.asElement(type);
            returnType = rt.toString();
        }
        return returnType;
    }

    private void note(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private static String printException(Throwable e) {
        StringWriter string = new StringWriter();
        PrintWriter writer = new PrintWriter(string);
        e.printStackTrace(writer);
        writer.flush();
        string.flush();
        return e.getMessage() + "\r\n" + string.toString();
    }

}
