package com.androidstudy.alfoneshub.models;

/**
 * Created by wahyu on 13/10/16.
 */

public class MenuNavigation17Model {
    private String menuName;
    private int menuIcon;
    private boolean isSelected = false;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
