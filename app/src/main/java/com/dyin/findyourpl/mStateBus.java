package com.dyin.findyourpl;

public class mStateBus {

    private boolean selectFinish ;
    private boolean arriveNow ;
    private boolean parking ;
    private boolean leaveNow ;
    private int selectId;

    public mStateBus(boolean first,boolean second,boolean third, boolean fourth){
        this.selectFinish=first;
        this.arriveNow =second;
        this.parking = third;
        this.leaveNow = fourth;
    }

    public boolean isSelectFinish(){
        return selectFinish;
    }

    public boolean isArriveNow(){
        return arriveNow;
    }

    public boolean isParking(){
        return parking;
    }

    public boolean isLeaveNow(){
        return leaveNow;
    }

    public int getSelectId(){return selectId;}

    public void setSelectId(int i){selectId=i;}

    public mStateBus(int i){
        switch (i){
            case 1:{
                this.selectFinish=true;
                this.arriveNow=false;
                this.parking = false;
                this.leaveNow= false;
                break;
            }
            case 2:{
                this.selectFinish=false;
                this.arriveNow=true;
                this.parking = false;
                this.leaveNow= false;
                break;
            }
            case 3:{
                this.selectFinish=false;
                this.arriveNow=false;
                this.parking = true;
                this.leaveNow= false;
                break;
            }
            case 4:{
                this.selectFinish=false;
                this.arriveNow=false;
                this.parking = false;
                this.leaveNow= true;
                break;
            }
            default:{
                this.selectFinish=false;
                this.arriveNow=false;
                this.parking = false;
                this.leaveNow= false;
                break;
            }
        }
    }

    public int getParkingState(){
        if(isSelectFinish())
            return 1;
        if(isArriveNow())
            return 2;
        if(isParking())
            return 3;
        if(isLeaveNow())
            return 4;
        return 0;
    }


}
