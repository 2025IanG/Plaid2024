package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class DefaultDriveCommand extends Command {

    private final DriveSubsystem m_driveSubsystem;
    private final XboxController m_controller;

    public DefaultDriveCommand(DriveSubsystem driveSubsystem, XboxController controller) {
        
        m_driveSubsystem = driveSubsystem;
        addRequirements(m_driveSubsystem);

        m_controller = controller;

    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        m_driveSubsystem.arcadeDrive(
            MathUtil.applyDeadband(m_controller.getLeftY(), 0.1), 
            MathUtil.applyDeadband(m_controller.getLeftX(), 0.1)
        );
    }

    @Override
    public void end(boolean interrupted) {
        m_driveSubsystem.stopAllMotors();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
