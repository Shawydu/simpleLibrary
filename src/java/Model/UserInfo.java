/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Model;

/**
*
* @author kezhang
*/
public class UserInfo {

private int UserID;
private String FirstName;
private String LastName;
private String Password;
private int TypeID;

public int getUserID() {
    return UserID;
}

public void setUserID(int UserID) {
    this.UserID = UserID;
}

public String getFirstName() {
    return FirstName;
}

public void setFirstName(String FirstName) {
    this.FirstName = FirstName;
}

public String getLastName() {
    return LastName;
}

public void setLastName(String LastName) {
    this.LastName = LastName;
}

public String getPassword() {
    return Password;
}

public void setPassword(String Password) {
    this.Password = Password;
}

public int getTypeID() {
    return TypeID;
}

public void setTypeID(int TypeID) {
    this.TypeID = TypeID;
}

}
