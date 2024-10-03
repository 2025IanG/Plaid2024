package frc.robot.commands;

import frc.robot.subsystems.PusherSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

public class PusherBackward extends Command {

    private PusherSubsystem m_pusherSubsystem;
    
    public PusherBackward(PusherSubsystem pushersubsystem) {
        m_pusherSubsystem = pushersubsystem;
        addRequirements(pushersubsystem);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        m_pusherSubsystem.setPusherMotor(-0.4);
    }

    @Override
    public boolean isFinished() {
        return false; //m_pusherSubsystem.isPusherSwitchPressed();
    }

    @Override
    public void end(boolean interrupted) {
        m_pusherSubsystem.stopPusherMotor();
    }
}
