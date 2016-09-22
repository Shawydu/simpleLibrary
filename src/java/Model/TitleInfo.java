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
public class TitleInfo {
    private String Isbn;
    private String Author;
    private String TitleName;
    
    public String getIsbn() {
        return Isbn;
    }

    public void setIsbn(String Isbn) {
        this.Isbn = Isbn;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String Author) {
        this.Author = Author;
    }

    public String getTitleName() {
        return TitleName;
    }

    public void setTitleName(String TitleName) {
        this.TitleName = TitleName;
    }
}
