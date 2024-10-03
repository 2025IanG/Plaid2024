package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DoorSubsystem;

public class DoorUp extends Command {

    private DoorSubsystem m_doorSubsystem;
    
    public DoorUp(DoorSubsystem doorsubsystem) {
        m_doorSubsystem = doorsubsystem;
        addRequirements(doorsubsystem);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        m_doorSubsystem.setDoorMotor(0.4);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_doorSubsystem.stopDoorMotor();
    }
    
}
