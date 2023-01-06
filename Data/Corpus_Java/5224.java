package pl.edu.amu.wmi.daut.re;

import junit.framework.TestCase;

/**
 * Testy klasy LinuxFileMask.
 */
public class TestLinuxFileMask extends TestCase {

    /**
     * Test dla plikow o dowolnej nazwie.
     */
    public final void testAllFilesMask() {
        LinuxFileMask linuxMask = new LinuxFileMask("*");

        assertTrue(linuxMask.accepts(""));
        assertTrue(linuxMask.accepts("a"));
        assertTrue(linuxMask.accepts("*"));
        assertTrue(linuxMask.accepts("abc"));
        assertTrue(linuxMask.accepts("abc.xml"));
        assertTrue(linuxMask.accepts("123@#$.123#@$#@"));
        assertTrue(linuxMask.accepts("a.b.c.*.&.(.)"));
        assertTrue(linuxMask.accepts("abcdefghijklmnopqrstuwyz1234567890"));
    }

    /**
     * Test dla plikow, o nazwie skladajacej sie z dokladnie jednego znaku.
     */
    public final void testTwoCharFileMask() {
        LinuxFileMask linuxMask = new LinuxFileMask("??");

        assertTrue(linuxMask.accepts("?X"));
        assertTrue(linuxMask.accepts("aa"));
        assertTrue(linuxMask.accepts("12"));
        assertTrue(linuxMask.accepts("%#"));
        assertTrue(linuxMask.accepts(". "));
        assertTrue(linuxMask.accepts("#1"));
        assertTrue(linuxMask.accepts("  "));
        assertFalse(linuxMask.accepts("fail"));
        assertFalse(linuxMask.accepts(""));
    }

    /**
     * Test dla plikow, o nazwie skladajacej sie z jednego, niepustego znaku
     * i dowolnej ilosci innych znakow.
     */
    public final void testCombinedOneCharAndAllFileMask() {

        LinuxFileMask linuxMask = new LinuxFileMask("?*");

        assertTrue(linuxMask.accepts("?"));
        assertTrue(linuxMask.accepts("aa"));
        assertTrue(linuxMask.accepts("1*"));
        assertTrue(linuxMask.accepts("%abc"));
        assertTrue(linuxMask.accepts(".abc.xml"));
        assertTrue(linuxMask.accepts("abcdefghijklmnopqrstuwyz1234567890"));
        assertFalse(linuxMask.accepts(""));
    }

    /**
     * Test dla wszystkich plikow o dowolnej nazwie i dowolnym rozszerzeniu.
     */
    public final void testAllFilesAllExtentionsMask() {

        LinuxFileMask linuxMask = new LinuxFileMask("*.*");

        assertTrue(linuxMask.accepts("."));
        assertTrue(linuxMask.accepts("a.a"));
        assertTrue(linuxMask.accepts("image.png"));
        assertTrue(linuxMask.accepts("cpp.net"));
        assertTrue(linuxMask.accepts("asp.net"));
        assertTrue(linuxMask.accepts("wpf.net"));
        assertTrue(linuxMask.accepts("mail@gmail.com"));
        assertTrue(linuxMask.accepts("($|4::}.JIO#$"));
        assertFalse(linuxMask.accepts(""));
        assertFalse(linuxMask.accepts("asafa$%$#"));
        assertFalse(linuxMask.accepts("some,none"));
        assertFalse(linuxMask.accepts("imageDOTpng"));
        assertFalse(linuxMask.accepts(""));
    }

    /**
     * Test dla wszystkich plikow o dowolnej nazwie i rozszerzeniu png.
     */
    public final void testAllFilesOfOneExtentionsMask() {

        LinuxFileMask linuxMask = new LinuxFileMask("*.png");

        assertTrue(linuxMask.accepts(".png"));
        assertTrue(linuxMask.accepts("a.png"));
        assertTrue(linuxMask.accepts("image.png"));
        assertTrue(linuxMask.accepts("cpp.png"));
        assertTrue(linuxMask.accepts("mail@gmail.png"));
        assertTrue(linuxMask.accepts("($|4::}.png"));
        assertFalse(linuxMask.accepts("."));
        assertFalse(linuxMask.accepts("image.jpg"));
        assertFalse(linuxMask.accepts("image.peengje"));
        assertFalse(linuxMask.accepts("asafa$%$#"));
        assertFalse(linuxMask.accepts("imageDOTpng"));
        assertFalse(linuxMask.accepts(""));
    }

    /**
     * Test dla wszystkich plikow o nazwie image i dowolnym rozszerzeniu.
     */
    public final void testAllExtentionsOneFileNameMask() {

        LinuxFileMask linuxMask = new LinuxFileMask("image.*");

        assertTrue(linuxMask.accepts("image.png"));
        assertTrue(linuxMask.accepts("image.jpg"));
        assertTrue(linuxMask.accepts("image.png"));
        assertTrue(linuxMask.accepts("image.peengje"));
        assertTrue(linuxMask.accepts("image.$%@#$@$"));
        assertTrue(linuxMask.accepts("image.tojestrozszerzeniepliku"));
        assertTrue(linuxMask.accepts("image."));
        assertFalse(linuxMask.accepts("($|4::}.png"));
        assertFalse(linuxMask.accepts("plik.png"));
        assertFalse(linuxMask.accepts("."));
        assertFalse(linuxMask.accepts("asafa$%$#"));
        assertFalse(linuxMask.accepts("imageDOTpng"));
        assertFalse(linuxMask.accepts(""));
    }

    /**
     * Test dla wszystkich plikow o nazwie plikX.txt, gdzie w miejsce X
     * wstawiony moze byc dowolny symbol.
     */
    public final void testAllSimilarFilesNamesMask() {

        LinuxFileMask linuxMask = new LinuxFileMask("plik?.txt");

        assertTrue(linuxMask.accepts("plik0.txt"));
        assertTrue(linuxMask.accepts("plik1.txt"));
        assertTrue(linuxMask.accepts("plik2.txt"));
        assertTrue(linuxMask.accepts("plik3.txt"));
        assertTrue(linuxMask.accepts("plik4.txt"));
        assertTrue(linuxMask.accepts("plik$.txt"));
        assertFalse(linuxMask.accepts("plik1.png"));
        assertFalse(linuxMask.accepts("plik10.txt"));
        assertFalse(linuxMask.accepts("plik1."));
        assertFalse(linuxMask.accepts("."));
        assertFalse(linuxMask.accepts("asafa$%$#"));
        assertFalse(linuxMask.accepts(""));
    }
}
