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
//import jdk.internal.platform.Container;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
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

import com.ctre.phoenix.music.Orchestra;

//com.ctre.phoenix.music.Orchestra.Orchestra(Collection<FrontLeft> instruments
//string C:\Users\htown\Documents\GitHub\mateos git things\5232-Octacanum-2021\5232 Octacanum 2021\src\main\deploy\nuh.chrp
//)



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  Orchestra _orchestra;
  //Motor declaring and motor control variables
  TalonFX FrontLeft = new TalonFX(16);
  TalonFX FrontRight = new TalonFX(15);
  TalonFX BackLeft = new TalonFX(17);
  TalonFX BackRight = new TalonFX(18);

  //using the motors with the WPI allows it to work with the tank drive function among other things
  WPI_TalonFX m_FrontLeft = new WPI_TalonFX(16);
  WPI_TalonFX m_FrontRight = new WPI_TalonFX(15);
  WPI_TalonFX m_BackLeft = new WPI_TalonFX(17);
  WPI_TalonFX m_BackRight = new WPI_TalonFX(18);

  //these are for the math that comes when controlling the motors instead of having to do the math within the motor controlling lines
  double FL = 0;
  double FR = 0;
  double BL = 0;
  double BR = 0;

  //controller declaring and setting up buttons
  private final Joystick m_stick = new Joystick(0);
  Joystick m_buttons = new Joystick(0);
  final JoystickButton k3 = new JoystickButton(m_buttons, 10);
  //variables that are assigned to each axis of the joystick
  public double Y;
  public double X;
  public double Z;

  int fans;


  int loopTime;
  //Pigeon IMU declaring and variables for it
  PigeonIMU _pigeon = new PigeonIMU(0);
  double InternalZ;
  double TurnGoal;
  double MRot;
  double [] ypr = new double[3];

  //auto variables
  int FirstGoal;
  double multi;
  double RotMulti;
  
  //motor value for auto
  double FRlevel;
  double FLlevel;

  //motor encoder value variables for auto
  int FRpos;
  int FLpos;
  

  //pnumatics decleration and variables
  int compr;
  int button;
  DoubleSolenoid doubleSolenoid = new DoubleSolenoid(0,1);
  int FLencoder;
  Compressor compressor = new Compressor(0);

  //setup code for the tank drive mode
  private final DifferentialDrive m_drive = new DifferentialDrive(m_FrontLeft,m_FrontRight);
  Relay fan1 = new Relay(0);
  //Relay fan2 = new Relay(0)


  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    //sets up the motors to brake rather then coast when no power is applied
    FrontLeft.setNeutralMode(NeutralMode.Brake);
    FrontRight.setNeutralMode(NeutralMode.Brake);
    BackRight.setNeutralMode(NeutralMode.Brake);
    BackLeft.setNeutralMode(NeutralMode.Brake);
    
    //makes the back motors follow the front ones in the tank drive because it uses the WPI
    m_BackLeft.follow(m_FrontLeft);
    m_BackRight.follow(m_FrontRight);

    //just a quick line so you can see when the robot init code happens
    System.out.println("-----------------Start of the program-----------------");
    _pigeon.setYaw(0);
   // _orchestra = new Orchestra(FrontRight);
  }
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    /*
    //resetting encoder values
    m_FrontLeft.setSelectedSensorPosition(0);
    m_FrontRight.setSelectedSensorPosition(0);
    m_BackLeft.setSelectedSensorPosition(0);
    m_BackRight.setSelectedSensorPosition(0);
    int kTimeoutMs = 50;
    _pigeon.setYaw(0,kTimeoutMs);

    //I use these variables to quickly find and set the goal and proportional multiplyer
    //proportional multiplyer means that it ramps down speed proportionally to how close the sensor position is to the goal position
    FirstGoal = 130000;
    multi = .0000035;

    //todo: learn how to have this is another file so it doesnt take up space here
    //this part corrects for it moving past it V
    while (FRpos + FLpos < (FirstGoal *2) - 55900){
      //this gets the encoder position and reverses the other one because the motors are reversed from left to right
      FRpos = FrontRight.getSelectedSensorPosition();
      FLpos = FrontLeft.getSelectedSensorPosition() * -1;
      //does the math of what the right side percent output is
      FRlevel = (FirstGoal - FRpos)*multi;
      if (FRpos != FirstGoal){
        FrontRight.set(ControlMode.PercentOutput, FRlevel);
      }
      //does the math of what the left side percent output is
      FLlevel = (FirstGoal - FLpos)*multi;
      FLlevel = FLlevel * -1;
      if (FLpos != FirstGoal){
        FrontLeft.set(ControlMode.PercentOutput, FLlevel);
      }
      }
      
      //same as above but for turning
      TurnGoal = 195;
      RotMulti = .005;
    
    
      //rotation code
    while ((TurnGoal - InternalZ)>15){
      _pigeon.getYawPitchRoll(ypr);
      System.out.println("yaw is " + ypr[0]);
      InternalZ = ypr[0];
      MRot = (TurnGoal - InternalZ) * RotMulti;
      
      //checks if you are turning left or right and changes the motor math output so it turns in the correct direction 
      if (TurnGoal < 0){//right
        MRot = MRot * -1;
        FrontLeft.set(ControlMode.PercentOutput, MRot);
        FrontRight.set(ControlMode.PercentOutput, MRot);
      }
      else{//left
        FrontLeft.set(ControlMode.PercentOutput, MRot);
        FrontRight.set(ControlMode.PercentOutput, MRot);
      }

    }
    */

  }

  @Override
  public void autonomousPeriodic() {
    loopTime = 0;
    //doubleSolenoid.set(Value.kForward);
    if (loopTime < 1){
      System.out.println("hey");
      loopTime = 2;
      //resetting encoder values
    m_FrontLeft.setSelectedSensorPosition(0);
    m_FrontRight.setSelectedSensorPosition(0);
    m_BackLeft.setSelectedSensorPosition(0);
    m_BackRight.setSelectedSensorPosition(0);
    int kTimeoutMs = 50;
    _pigeon.setYaw(0,kTimeoutMs);

    //I use these variables to quickly find and set the goal and proportional multiplyer
    //proportional multiplyer means that it ramps down speed proportionally to how close the sensor position is to the goal position
    FirstGoal = 200000;
    multi = .0000035;

    //todo: learn how to have this is another file so it doesnt take up space here
    //this part corrects for it moving past it V
    while (FRpos + FLpos < (FirstGoal *2) - 55900){
      //this gets the encoder position and reverses the other one because the motors are reversed from left to right
      FRpos = FrontRight.getSelectedSensorPosition();
      FLpos = FrontLeft.getSelectedSensorPosition() * -1;
      //does the math of what the right side percent output is
      FRlevel = (FirstGoal - FRpos)*multi;
      if (FRpos != FirstGoal){
        FrontRight.set(ControlMode.PercentOutput, FRlevel);
      }
      //does the math of what the left side percent output is
      FLlevel = (FirstGoal - FLpos)*multi;
      FLlevel = FLlevel * -1;
      if (FLpos != FirstGoal){
        FrontLeft.set(ControlMode.PercentOutput, FLlevel);
      }
      }
      
      //same as above but for turning
      TurnGoal = -105;
      RotMulti = .005;
    
    
      //rotation code
    while (Math.abs((TurnGoal - InternalZ))>15){
      _pigeon.getYawPitchRoll(ypr);
      System.out.println("yaw is " + ypr[0]);
      InternalZ = ypr[0];
      MRot = (TurnGoal - InternalZ) * RotMulti;
      
      //checks if you are turning left or right and changes the motor math output so it turns in the correct direction 
      if (TurnGoal < 0){//right
        //MRot = MRot * -1;
        FrontLeft.set(ControlMode.PercentOutput, MRot);
        FrontRight.set(ControlMode.PercentOutput, MRot);
      }
      else{//left
        FrontLeft.set(ControlMode.PercentOutput, MRot);
        FrontRight.set(ControlMode.PercentOutput, MRot);
      }

    }


    }
    loopTime = 2;    
  }

  @Override
  public void teleopInit() {
    //this sets up the variables that are used to select stuff
    button = 1;
    compr = 1;
    int kTimeoutMs = 50;
    _pigeon.setYaw(0,kTimeoutMs);
    System.out.println("print");

    _pigeon.getYawPitchRoll(ypr);
    System.out.println(ypr[0]);
    fans = 0;

  }

  @Override
  public void teleopPeriodic() {
    //here it sets up when the buttons on the controllers are pressed
    
    _pigeon.getYawPitchRoll(ypr);
    System.out.println(ypr[0]);
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
    if (m_stick.getRawButton(7)){
      fans = 0;
    }
    if (m_stick.getRawButton(8)){
      fans = 1;
    }
    //here it turns on and off the compressor
    if (compr == 1){
      compressor.start();
    }
    else{
      compressor.stop();
    }
    if (fans == 1){
      fan1.set(Relay.Value.kForward);
    }
    else{
      fan1.set(Relay.Value.kReverse);
    }

    //this sets the X Y and Z of the joystick each period
    X = m_stick.getX();
    Y = m_stick.getY();
    Z = m_stick.getZ();
    
    //this sets up deadzones with Math.abs(var) to get the absolute value
    if (Math.abs(X) <= .1){
      X = 0;
    }
    if (Math.abs(Y) <= .1){
      Y = 0;
    }
    if (Math.abs(Z) <= .1){
      Z = 0;
    }
    //this reverses the Z and X so it works with my code
    Z = -Z;
    X = -X;

    //this is if the button is in mechanum mode
    if (button == -1){
      //opens the solenoid to the mechanum mode
      doubleSolenoid.set(Value.kReverse);

      //this does the "turbo mode" so you can tune it seperatly from the tank drive
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
    //com.ctre.phoenix.music.Orchestra.Orchestra(Collection<FrontLeft, FrontRight>)
    //Orchestra =  _Orchestra(Collection<m_FrontLeft>, String C:\Users\htown\Documents\GitHub\mateos git things\5232-Octacanum-2021\5232 Octacanum 2021\src\main\deploy\nuh.chrp);

  }

  @Override
  public void testPeriodic(){
    fan1.set(Relay.Value.kForward);

     



  }

}
