package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.Constants.PusherConstants;

public class PusherSubsystem extends SubsystemBase {
    private final TalonFX pusherMotor;

    // private final DigitalInput pusherLimitSwitch;

    public PusherSubsystem() {

        pusherMotor = new TalonFX(PusherConstants.pusherMotorID);
        pusherMotor.setNeutralMode(NeutralModeValue.Brake);
        pusherMotor.setInverted(true);

        // pusherLimitSwitch = new DigitalInput(PusherConstants.pusherSwitchPort);

    }

    public void setPusherMotor(double speed) {
        pusherMotor.set(speed);
    }

    public void stopPusherMotor() {
        pusherMotor.stopMotor();
    }

    // public boolean isPusherSwitchPressed() {
    //     return pusherLimitSwitch.get();
    // }
    
}

