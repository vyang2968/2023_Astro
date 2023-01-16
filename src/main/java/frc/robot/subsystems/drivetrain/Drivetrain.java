package frc.robot.subsystems.drivetrain;

import java.util.List;

import com.ctre.phoenix.music.Orchestra;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.drivetrain;

public class Drivetrain extends SubsystemBase {

  private static Drivetrain instance;
  public static synchronized Drivetrain getInstance(){
    if (instance == null) instance = new Drivetrain();
    return instance;
  }

  private SwerveModule frontRight = new SwerveModule(1, 2, 21, 0);
  private SwerveModule frontLeft = new SwerveModule(3, 4, 22, 0);
  private SwerveModule backLeft = new SwerveModule(5, 6, 23, 0);
  private SwerveModule backRight = new SwerveModule(7, 8, 24, 0);

  private Orchestra orchestra = new Orchestra(
    List.of(frontRight.getSpeedMotor(), frontRight.getAngleMotor(), 
            frontLeft.getSpeedMotor(), frontLeft.getAngleMotor(), 
            backLeft.getSpeedMotor(), backLeft.getAngleMotor(), 
            backRight.getSpeedMotor(), backRight.getAngleMotor())
    );


  private Drivetrain() {
    orchestra.loadMusic("cbat.chrp");
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Speed Motor Velocity", frontLeft.getModuleState().speedMetersPerSecond);
    SmartDashboard.putNumber("Speed Motor Angle", frontLeft.getModuleState().angle.getDegrees());
  }

  public void playMusic() {
    orchestra.play();
  }
}