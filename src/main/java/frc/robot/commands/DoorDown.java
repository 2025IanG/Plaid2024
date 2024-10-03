package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DoorSubsystem;

public class DoorDown extends Command {

    private DoorSubsystem m_doorSubsystem;
    
    public DoorDown(DoorSubsystem doorsubsystem) {
        m_doorSubsystem = doorsubsystem;
        addRequirements(doorsubsystem);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        m_doorSubsystem.setDoorMotor(-0.4);
    }

    @Override
    public boolean isFinished() {
        return false; //m_doorSubsystem.isDoorSwitchPressed();
    }

    @Override
    public void end(boolean interrupted) {
        m_doorSubsystem.stopDoorMotor();
    }
    
}
