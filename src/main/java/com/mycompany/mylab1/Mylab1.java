/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mylab1;

import com.mycompany.mylab1.Model.*;
import com.mycompany.mylab1.View.*;
import com.mycompany.mylab1.Controller.*;

public class Mylab1 {

    public static void main(String[] args) {
        Model model = new Model();
        MainFrame view = new MainFrame();
        new Controller(model, view);
        //

    }
}
