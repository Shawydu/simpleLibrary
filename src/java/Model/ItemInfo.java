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
public class ItemInfo {

    private int ItemID;
    private String Isbn;
    private TitleInfo Title;

    public TitleInfo getTitle() {
        return Title;
    }

    public void setTitle(TitleInfo Title) {
        this.Title = Title;
    }
    
    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int ItemID) {
        this.ItemID = ItemID;
    }

    public String getIsbn() {
        return Isbn;
    }

    public void setIsbn(String Isbn) {
        this.Isbn = Isbn;
    } 

}
