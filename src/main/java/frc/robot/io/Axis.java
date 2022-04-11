package frc.robot.io;

public enum Axis{

    LEFT_X(0), LEFT_Y(1), LEFT_TRIGGER(2), RIGHT_TRIGGER(3), RIGHT_X(4), RIGHT_Y(5);

    private int mID;

    Axis(int ID){
        mID = ID;
    }

    public int getID(){
        return mID;
    }


}

