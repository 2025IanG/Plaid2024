package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.Constants.DoorConstants;

public class DoorSubsystem extends SubsystemBase {
    private final TalonFX doorMotor;

    // private final DigitalInput doorLimitSwitch;

    public DoorSubsystem() {

        doorMotor = new TalonFX(DoorConstants.doorMotorID);
        doorMotor.setNeutralMode(NeutralModeValue.Brake);
        doorMotor.setInverted(true);

        // doorLimitSwitch = new DigitalInput(DoorConstants.doorSwitchPort);

    }

    public void setDoorMotor(double speed) {
        doorMotor.set(speed);
    }

    public void stopDoorMotor() {
        doorMotor.stopMotor();
    }

    // public boolean isDoorSwitchPressed() {
    //     return doorLimitSwitch.get();
    // }
    
}
