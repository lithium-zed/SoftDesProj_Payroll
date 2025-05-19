package org.example;

public class Compliance {
   private double monthly, SSS, BIR, Philhealth, Pagibig, netpay, annually;

   public Compliance(double monthly){
       this.monthly = monthly;
       this.annually = monthly * 12;
       this.SSS = computeSSS();
       this.Philhealth = computePhilhealth();
       this.Pagibig = computePagIbig();
       computeBir();
       computeNetpay();
   }

    public double getMonthly() {
        return monthly;
    }

    public void setMonthly(double monthly) {
        this.monthly = monthly;
    }

    public double getSSS() {
        return SSS;
    }

    public void setSSS(double SSS) {
        this.SSS = SSS;
    }

    public double getBIR() {
        return BIR;
    }

    public void setBIR(double BIR) {
        this.BIR = BIR;
    }

    public double getPhilhealth() {
        return Philhealth;
    }

    public void setPhilhealth(double philhealth) {
        Philhealth = philhealth;
    }

    public double getPagibig() {
        return Pagibig;
    }

    public void setPagibig(double pagibig) {
        Pagibig = pagibig;
    }

    public double getNetpay() {
        return netpay;
    }

    public void setNetpay(double netpay) {
        this.netpay = netpay;
    }

    public double getAnnually() {
        return annually;
    }

    public void setAnnually(double annually) {
        this.annually = annually;
    }
    public double computeSSS(){
     double msc = 0;
     if (monthly >= 5000 || monthly <= 35000 ){
      msc = monthly * 0.05;
     }else if (monthly < 5000){
         msc = 5000 * 0.05;
         return msc;
     } else if (monthly > 35000){
          msc = 35000 * 0.05;
         return msc;
     }
        return msc;
    }
    public double computePhilhealth(){
       double msc = 0;
       if (monthly < 10000){
           msc = 10000 * 0.025;
       } else if (monthly > 100000){
           msc = 100000 * 0.025;
       } else {
           msc = monthly * 0.025;
       }
       return msc;
    }
    public double computePagIbig(){
       double msc = 0;
       if (monthly < 5000){
           msc = 0;
       }
       else if (monthly > 10000){
           msc = 10000 * 0.02;
       } else {
           msc = monthly * 0.02;
       }
       return msc;
    }
    public void computeBir(){
       double annual = monthly * 12;
       double annualtax = 0;
   if (annual <= 250000){
       annualtax = 0;
   } else if (annual <= 400000){
       annualtax = annual * 0.15;
   } else if (annual <= 800000){
       annualtax = 22500 + (annual - 400000) * 0.20;
   } else if (annual <= 2000000){
       annualtax = 102500 + (annual - 800000) * 0.25;
   } else if (annual <= 8000000){
       annualtax = 402500 + (annual - 2000000) * 0.30;
   }else {
       annualtax = 2202500 + (annual - 8000000) * 0.35;
   }
       this.BIR = annualtax / 12;
    }
    public void computeNetpay(){
       this.netpay = this.monthly - (this.SSS + this.Philhealth + this.Pagibig + this.BIR);
    }
}