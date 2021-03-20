/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Rot extends CommandBase {
  /**
   * Creates a new Rot.
   */
  double FirstGoal;
  double multi;
  double RotMulti;
  double goalFt;
  int loopTime;
  PigeonIMU _pigeon = new PigeonIMU(0);
  double InternalZ;
  double TurnGoal;
  double MRot;
  double [] ypr = new double[3];
  double FRlevel;
  double FLlevel;
  int FRpos;
  int FLpos;
  boolean forwardDone;

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

  public Rot() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    forwardDone = false;
    loopTime = 0;
    goalFt = 0;
    TurnGoal = 0;
    m_FrontLeft.setSelectedSensorPosition(0);
    m_FrontRight.setSelectedSensorPosition(0);
    int kTimeoutMs = 50;
    _pigeon.setYaw(0,kTimeoutMs);
    FrontLeft.setNeutralMode(NeutralMode.Brake);
    FrontRight.setNeutralMode(NeutralMode.Brake);
    BackRight.setNeutralMode(NeutralMode.Brake);
    BackLeft.setNeutralMode(NeutralMode.Brake);
    
    //makes the back motors follow the front ones in the tank drive because it uses the WPI
    m_BackLeft.follow(m_FrontLeft);
    m_BackRight.follow(m_FrontRight);
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println("what");
    while (loopTime < 8){
      
      //this was just recently added so every time it loops it changes the what setpoints it is going for.  I realise that I couldve done this with other stuff so it doesnt have to be in robot.java, but it is here for now
      //negative angle for right turning
      
      if (loopTime == 1){
        //I keep this here so I can refer to it later: FirstGoal = 200000;
        goalFt = 6;
        FirstGoal = 165000;
        //goal ft would ussally be 6 feet but im testing something
        TurnGoal = -105;
      }
      else if (loopTime == 2){
        goalFt = 5;
        FirstGoal = 41250;
        TurnGoal = 105;
      }
      else if (loopTime == 3){
        goalFt = 3;
        TurnGoal = 105;
      }
      else if (loopTime == 4){
        goalFt = 3;
        TurnGoal = 105;
      }
      else if (loopTime == 5){
        goalFt = 3;
        TurnGoal = 105;
      }
      else if (loopTime == 6){
        goalFt = 1.5;
        TurnGoal = -105;
      }
      else if (loopTime == 7){
        goalFt = 6;
        TurnGoal = 195;
      }
      else{

      }


      //this is based on the first measurements I took, they may need to be tuned
      //found by deviding the total encoder value took when I tried to go to 6 ft (200000 tics) then I devided it by 6 for 6 feet
      
      //FirstGoal =  goalFt * 27500;
      
      //System.out.println(FirstGoal);
      //schoolsong on the motors
/*
      //resetting encoder and pigeon values
      m_FrontLeft.setSelectedSensorPosition(0);
      m_FrontRight.setSelectedSensorPosition(0);
      m_BackLeft.setSelectedSensorPosition(0);
      m_BackRight.setSelectedSensorPosition(0);
      int kTimeoutMs = 50;
      _pigeon.setYaw(0,kTimeoutMs);
*/
      //I use these variables to quickly find and set the goal and proportional multiplyer
      //proportional multiplyer means that it ramps down speed proportionally to how close the sensor position is to the goal position
      multi = .0000035;
      RotMulti = .005;

      //the minusing part corrects for it moving past the goal or it going to slow
      //System.out.println(forwardDone);
      if(forwardDone == false){
        //System.out.println("this is the " + loopTime);
        if (FRpos + FLpos < (FirstGoal *2) - 55900){
          forwardDone = false;
          
        
          //this gets the encoder position and reverses the other one because the motors are reversed from left to right
          FRpos = FrontRight.getSelectedSensorPosition();
          FLpos = FrontLeft.getSelectedSensorPosition() * -1;
          //does the math of what the percent output is per side
          FRlevel = (FirstGoal - FRpos)*multi;
          FLlevel = (FirstGoal - FLpos)*multi;
          System.out.println("Right: "+ FRpos);
          System.out.print(" Left: "+ FLpos);
          //reverses left side bc motors are backwards
          FLlevel = FLlevel * -1;
          

          if (FRpos != FirstGoal){
          FrontRight.set(ControlMode.PercentOutput, FRlevel); 
          }
          
          if (FLpos != FirstGoal){
            FrontLeft.set(ControlMode.PercentOutput, FLlevel);
          }
        
        }
        else{
          forwardDone = true;
        }
     }
      
      //same as above but for turning
      if (forwardDone == true){
        if (Math.abs((TurnGoal - InternalZ))>15){
          _pigeon.getYawPitchRoll(ypr);
          //System.out.println("yaw is " + ypr[0]);
          InternalZ = ypr[0];
          //MRot is motor rotation wich is what I set the motors to
          MRot = (TurnGoal - InternalZ) * RotMulti;
          FrontLeft.set(ControlMode.PercentOutput, MRot);
          FrontRight.set(ControlMode.PercentOutput, MRot);
          m_FrontLeft.setSelectedSensorPosition(0);
          m_FrontRight.setSelectedSensorPosition(0);
        }
        else{
          forwardDone = false;
          System.out.println("erterg;lkjdsnhfg;lkjsdfga;sldkjf;lkajsd;lkjfa;lksdj;lkajsdflknanerlajenrfkaejrngekajnrt");
          loopTime = loopTime + 1;
          //resetting encoder and pigeon values
          //this may be causing issues in that it takes too long to reset the encoders
          m_FrontLeft.setSelectedSensorPosition(0);
          m_FrontRight.setSelectedSensorPosition(0);
          //int kTimeoutMs = 50;
          _pigeon.setYaw(0);
          //_pigeon.setYaw(0,kTimeoutMs);
        }
      }
      else{
        forwardDone = false;
      }

    }   
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
