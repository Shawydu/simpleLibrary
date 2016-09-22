/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Time;

/**
 *
 * @author kezhang
 */
public class LoanInfo implements Comparable<LoanInfo> {

    private int LoanID;
    private int ItemID;
    private int UserID;
    private double LatePayment;
    private Time StartTime;     // Designed for the 5 min expired requirement
    private Time ReturnTime;    // Designed for the 5 min expired requirement
    private Time DueTime;       // Designed for the 5 min expired requirement
    private int RenewTimes;    // Designed for the 5 min expired requirement
    private UserInfo User;

    public UserInfo getUser() {
        return User;
    }

    public void setUser(UserInfo User) {
        this.User = User;
    }
    private ItemInfo Item;

    public ItemInfo getItem() {
        return Item;
    }

    public void setItem(ItemInfo Item) {
        this.Item = Item;
    }

    public int getLoanID() {
        return LoanID;
    }

    public void setLoanID(int LoanID) {
        this.LoanID = LoanID;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int ItemID) {
        this.ItemID = ItemID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public double getLatePayment() {
        return LatePayment;
    }

    public void setLatePayment(double LatePayment) {
        this.LatePayment = LatePayment;
    }

    public Time getStartTime() {
        return StartTime;
    }

    public void setStartTime(Time StartTime) {
        this.StartTime = StartTime;
    }

    public Time getReturnTime() {
        return ReturnTime;
    }

    public void setReturnTime(Time ReturnTime) {
        this.ReturnTime = ReturnTime;
    }

    public Time getDueTime() {
        return DueTime;
    }

    public void setDueTime(Time DueTime) {
        this.DueTime = DueTime;
    }

    public int getRenewTimes() {
        return RenewTimes;
    }

    public void setRenewTimes(int RenewTimes) {
        this.RenewTimes = RenewTimes;
    }

    @Override
    public int compareTo(LoanInfo o) {
        return this.LoanID - o.getLoanID();
    }
}
