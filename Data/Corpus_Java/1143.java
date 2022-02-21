package org.ovirt.engine.core.bll.hostdeploy;

import org.ovirt.engine.core.bll.NonTransactiveCommandAttribute;
import org.ovirt.engine.core.bll.VdsCommand;
import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.bll.tasks.CommandCoordinatorUtil;
import org.ovirt.engine.core.bll.tasks.interfaces.CommandCallback;
import org.ovirt.engine.core.bll.validator.UpgradeHostValidator;
import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.action.ActionType;
import org.ovirt.engine.core.common.action.VdsActionParameters;
import org.ovirt.engine.core.common.errors.EngineMessage;

@NonTransactiveCommandAttribute
public class HostUpgradeCheckCommand<T extends VdsActionParameters> extends VdsCommand<T> {

    public HostUpgradeCheckCommand(T parameters, CommandContext cmdContext) {
        super(parameters, cmdContext);
    }

    @Override
    protected boolean validate() {
        UpgradeHostValidator validator = new UpgradeHostValidator(getVds());

        return validate(validator.hostExists())
                && validate(validator.statusSupportedForHostUpgradeCheck());
    }

    @Override
    protected void setActionMessageParameters() {
        addValidationMessage(EngineMessage.VAR__ACTION__UPGRADE__CHECK);
        addValidationMessage(EngineMessage.VAR__TYPE__HOST);
    }

    @Override
    protected void executeCommand() {
        CommandCoordinatorUtil.executeAsyncCommand(ActionType.HostUpgradeCheckInternal,
                withRootCommandInfo(new VdsActionParameters(getVdsId())),
                cloneContext());

        setSucceeded(true);
    }

    @Override
    public AuditLogType getAuditLogTypeValue() {
        return getSucceeded() ? AuditLogType.HOST_AVAILABLE_UPDATES_STARTED : AuditLogType.HOST_AVAILABLE_UPDATES_FAILED;
    }

    @Override
    public CommandCallback getCallback() {
        return new HostUpgradeCheckCallback();
    }
}
