/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.TimedRobot;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

//currently unused

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  TalonFX FXtest = new TalonFX(15);

  TalonFX FrontLeft = new TalonFX(16);
  TalonFX FrontRight = new TalonFX(15);
  TalonFX BackLeft = new TalonFX(17);
  TalonFX BackRight = new TalonFX(18);

  WPI_TalonFX m_FrontLeft = new WPI_TalonFX(16);
  WPI_TalonFX m_FrontRight = new WPI_TalonFX(15);
  
  WPI_TalonFX m_BackLeft = new WPI_TalonFX(17);
  
  

  WPI_TalonFX m_BackRight = new WPI_TalonFX(18);


  private final Joystick m_stick = new Joystick(0);
  Joystick m_buttons = new Joystick(0);

  final JoystickButton k3 = new JoystickButton(m_buttons, 10);

  PigeonIMU.GeneralStatus genStatus = new PigeonIMU.GeneralStatus();
  PigeonIMU Internal = new PigeonIMU(14);//edit this!!


  double FL = 0;
  double FR = 0;
  double BL = 0;
  double BR = 0;

  double FRlevel;
  double FLlevel;

  int FRpos;
  int FLpos;
  int FirstGoal;
  double AutoSpeed = .08;
  double AutoRSpeed = AutoSpeed;
  double AutoLSpeed = AutoSpeed * -1;
  double multi;

  public double Y = .1;
  public double X = 0;
  public double Z = 0;

  int compr;

  int button;

  DoubleSolenoid doubleSolenoid = new DoubleSolenoid(0,1);

  int FLencoder;


  Compressor compressor = new Compressor(0);
  /*


  public void setMaxOutput(double maxOutput) {
    m_drive.setMaxOutput(maxOutput * .8);
  }
  */


  private final DifferentialDrive m_drive = new DifferentialDrive(m_FrontLeft,m_FrontRight);


  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    
    FrontLeft.setNeutralMode(NeutralMode.Brake);
    FrontRight.setNeutralMode(NeutralMode.Brake);
    BackRight.setNeutralMode(NeutralMode.Brake);
    BackLeft.setNeutralMode(NeutralMode.Brake);
    
    m_BackLeft.follow(m_FrontLeft);
    m_BackRight.follow(m_FrontRight);
    System.out.println("-----------------Start of the program-----------------");
  }
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    m_FrontLeft.setSelectedSensorPosition(0);
    m_FrontRight.setSelectedSensorPosition(0);
    m_BackLeft.setSelectedSensorPosition(0);
    m_BackRight.setSelectedSensorPosition(0);

    FirstGoal = 130000;
    multi = .0000035;

    while (FRpos + FLpos < (FirstGoal *2) - 55900){


      FRpos = FrontRight.getSelectedSensorPosition();
      FLpos = FrontLeft.getSelectedSensorPosition() * -1;
    
      FRlevel = (FirstGoal - FRpos)*multi;
      if (FRpos != FirstGoal){
        FrontRight.set(ControlMode.PercentOutput, FRlevel);
      }
      
      FLlevel = (FirstGoal - FLpos)*multi;
      FLlevel = FLlevel * -1;
      if (FLpos != FirstGoal){
        FrontLeft.set(ControlMode.PercentOutput, FLlevel);
      }
      }
    

  }

  @Override
  public void autonomousPeriodic() {
    doubleSolenoid.set(Value.kForward);
    System.out.println(FRlevel);
    System.out.print("  ");
    System.out.print(FrontRight.getSelectedSensorPosition());
    System.out.print("  ");
    FRpos = FrontRight.getSelectedSensorPosition();
    FLpos = FrontLeft.getSelectedSensorPosition() * -1;    
  }

  @Override
  public void teleopInit() {
    button = 1;
    compr = 1;

  }

  @Override
  public void teleopPeriodic() {
    if (m_stick.getRawButton(4)){
      button = -1;
    }
    if (m_stick.getRawButton(3)){
      button = 1;
    }
    if (m_stick.getRawButton(10)){
      compr = -1;
    }
    if (m_stick.getRawButton(5)){
      compr = 1;
    }
    if (compr == 1){
      compressor.start();
    }
    else{
      compressor.stop();
    }

    X = m_stick.getX();
    Y = m_stick.getY();
    Z = m_stick.getZ();
    
    if (Math.abs(X) <= .1){
      X = 0;
    }
    if (Math.abs(Y) <= .1){
      Y = 0;
    }
    if (Math.abs(Z) <= .1){
      Z = 0;
    }
    Z = -Z;
    X = -X;

    

    /*
    
    dont use this anymore because I moved it seperatly
    
    if (m_stick.getRawButton(1)){
      X = X * 2;
      Y = Y * 2;
      Z = Z * 1.25;
    }
    else{
      X = X*.75;
      Y = Y*.75;
      Z = Z*.5;
    }
    */

    
    if (button == -1){
      doubleSolenoid.set(Value.kReverse);
      if (m_stick.getRawButton(1)){
        X = X * 2;
        Y = Y * 2;
        Z = Z * 1.25;
      }
      else{
        X = X*.5;
        Y = Y*.5;
        Z = Z*.25;
      }
      

      FL = Y+(X+Z);
      FR = Y-(X+Z);
      BL = Y-(X-Z);
      BR = Y+(X-Z);

      //reverses the RIGHT SIDE
      FR = -FR;
      BR = -BR;
    
      FrontLeft.set(ControlMode.PercentOutput,FL);
      FrontRight.set(ControlMode.PercentOutput,FR);
      BackLeft.set(ControlMode.PercentOutput,BL);
      BackRight.set(ControlMode.PercentOutput,BR);
    }
    else {
      doubleSolenoid.set(Value.kForward);
      if (m_stick.getRawButton(1)){
        X = X * 2;
        Y = Y * 2;
        Z = Z * 1.25;
      }
      else{
        X = X*.75;
        Y = Y*.75;
        Z = Z*.5;
      }

      m_drive.arcadeDrive(Y, Z);
      m_BackLeft.follow(m_FrontLeft);
      m_BackRight.follow(m_FrontRight);
    }


  }

  @Override
  public void disabledInit() {
    doubleSolenoid.set(Value.kOff);
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
    // look here for CAN testing to say its the wiring https://docs.ctre-phoenix.com/en/latest/ch08_BringUpCAN.html#approach-1-best
  }

  @Override
  public void testPeriodic(){
    System.out.println(genStatus);
    



  }

}
