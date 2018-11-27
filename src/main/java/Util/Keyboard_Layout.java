package Util;

// 각각의 키패드의 정보를 담는 Class
public class Keyboard_Layout {
    // 키보드의 값
    public String keyValue;
    // 기호 여부
    public boolean symbol = false;
    // x, y좌표, action_up시 좌표, 터치수
    public int x,y, x1, y1, count;
}
