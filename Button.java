public class Button {
    protected String text;
    protected boolean visible = true;
    protected int posX;
    protected int posY;
    protected int width = 200;
    protected int height = 50;

    boolean isClicked(int lastClickX, int lastClickY) {
        return lastClickX >= this.posX && lastClickX <= this.posX + width
            && lastClickY >= this.posY && lastClickY <= this.posY + height;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }

    public String getText() {
        return this.text;
    }
}

