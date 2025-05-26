package org.example;

public class Compliance {
   private double gross, SSS, BIR, Philhealth, Pagibig, netpay, annually,monthly,totaldeductions;

   public Compliance(double gross){
       this.gross = gross;
       this.monthly = gross;
       this.annually = monthly * 12;
       this.SSS = computeSSS(gross);
       this.Philhealth = computePhilhealth(gross);
       this.Pagibig = computePagIbig(gross);
       this.BIR = computeBir(gross);

       computeNetpay();
   }

    public double getTotaldeductions() {
        return totaldeductions;
    }

    public void setTotaldeductions(double totaldeductions) {
        this.totaldeductions = totaldeductions;
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


    public double computeSSS(double gross){
     double msc = 0;
     if (gross <= 0){
         return 0;
     }

     if (gross >= 5000 && gross <= 35000 ){
      msc = gross * 0.05;

     }else if (gross < 5000){
         msc = 5000 * 0.05;
     } else if (gross > 35000){
          msc = 35000 * 0.05;
     }
       return msc;
    }



    public double computePhilhealth(double gross){
       double msc = 0;
       if (gross <= 0){
           return 0;
       }
       if (gross < 10000){
           msc = 10000 * 0.025;
       } else if (gross > 100000){
           msc = 100000 * 0.025;
       } else {
           msc = gross * 0.025;
       }
       return msc;
    }
    public double computePagIbig(double gross){
       double msc = 0;
       if (gross <= 0){
           return 0;
       }
       if (gross < 5000){
           msc = 0;
       }
       else if (gross > 10000){
           msc = 10000 * 0.02;
       } else {
           msc = gross * 0.02;
       }
       return msc;
    }
    public double computeBir(double gross){
//       double annual = monthly * 12;
       double annualtax = 0;

   if (gross <= 250000){
       annualtax = 0;

   } else if (gross <= 400000){
       annualtax = gross * 0.15;

   } else if (gross <= 800000){
       annualtax = 22500 + (gross - 400000) * 0.20;
   } else if (gross <= 2000000){
       annualtax = 102500 + (gross - 800000) * 0.25;
   } else if (gross <= 8000000){
       annualtax = 402500 + (gross - 2000000) * 0.30;
   }else {
       annualtax = 2202500 + (gross - 8000000) * 0.35;
   }

       this.BIR = annualtax / 12;

        return annualtax;
    }
    public void computeNetpay(){
       totaldeductions = this.SSS + this.Philhealth + this.Pagibig + this.BIR;
       this.netpay = this.gross - (totaldeductions);
    }
}