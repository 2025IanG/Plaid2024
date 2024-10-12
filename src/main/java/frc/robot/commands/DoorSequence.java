package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DoorSubsystem;
import frc.robot.subsystems.PusherSubsystem;

public class DoorSequence extends Command {

    PusherSubsystem m_pusherSubsystem;
    DoorSubsystem m_doorSubsystem;

    public DoorSequence(PusherSubsystem pusherSubsystem, DoorSubsystem doorSubsystem) {

        m_pusherSubsystem = pusherSubsystem;
        m_doorSubsystem = doorSubsystem;

        addRequirements(pusherSubsystem, doorSubsystem);

    }

    public void initialize() {
        new SequentialCommandGroup(
            new DoorDown(m_doorSubsystem).withTimeout(0.5),
            new PusherForward(m_pusherSubsystem).withTimeout(0.5),
            new PusherBackward(m_pusherSubsystem).withTimeout(0.5),
            new DoorUp(m_doorSubsystem).withTimeout(0.5)
        );
    }

    public void execute() {}

    public boolean isFinished() {
        return true;
    }

    public void end(boolean interrupted) {
        m_pusherSubsystem.stopPusherMotor();
        m_doorSubsystem.stopDoorMotor();
    }

    public boolean runsWhenDisabled() {
        return false;
    }
    
}
