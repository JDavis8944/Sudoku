package ArtificialIntelligence;

/*
 * Sudoku
 * @author Joshua Davis
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import static java.awt.Font.BOLD;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public final class Sudoku extends JFrame implements ActionListener {

    private int[][] cellValue;
    private static int size = 9;
    private Grid grid;
    private SudokuGenerator generator;

    private JButton btnEasy = new JButton("Easy");
    private JButton btnMedium = new JButton("Medium");
    private JButton btnHard = new JButton("Hard");
    private JButton btnSolve = new JButton("Solve");
    private JButton btnClear = new JButton("Reset");
    private JRadioButton btnZero = new JRadioButton("0");
    private JRadioButton btnOne = new JRadioButton("1");
    private JRadioButton btnTwo = new JRadioButton("2");
    private JRadioButton btnThree = new JRadioButton("3");
    private JRadioButton btnFour = new JRadioButton("4");
    private JRadioButton btnFive = new JRadioButton("5");
    private JRadioButton btnSix = new JRadioButton("6");
    private JRadioButton btnSeven = new JRadioButton("7");
    private JRadioButton btnEight = new JRadioButton("8");
    private JRadioButton btnNine = new JRadioButton("9");
    private JPanel btnPanel = new JPanel(new GridLayout(5, 1));
    private JPanel numPanel = new JPanel(new GridLayout(1, 1));
    private boolean status = false;
    private int inputNumber = 0;
    private boolean inputInc = false;

    Sudoku(Grid grid) {
        super("Sudoku");
        this.grid = grid;

        generator = new SudokuGenerator(grid);
        cellValue = new int[size][size];

        Cell[][] cell = grid.getCells();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cell[i][j].addMouseListener(new Listener(i, j));
            }
        }

        btnEasy.addActionListener(e -> setDifficulty((int) (size * size * .55)));
        btnMedium.addActionListener(e -> setDifficulty((int) (size * size * .65)));
        btnHard.addActionListener(e -> setDifficulty((int) (size * size * .75)));
        btnSolve.addActionListener(e -> solve());
        btnClear.addActionListener(e -> clear());
        btnPanel.add(btnClear);
        btnPanel.add(btnEasy);
        btnPanel.add(btnMedium);
        btnPanel.add(btnHard);
        btnPanel.add(btnSolve);
        add(btnPanel, BorderLayout.EAST);

        btnZero.setMnemonic(KeyEvent.VK_0);
        btnZero.addActionListener(e -> setInputNumber(0));
        btnZero.setSelected(true);

        btnOne.setMnemonic(KeyEvent.VK_1);
        btnOne.addActionListener(e -> setInputNumber(1));
        btnTwo.setMnemonic(KeyEvent.VK_2);
        btnTwo.addActionListener(e -> setInputNumber(2));
        btnThree.setMnemonic(KeyEvent.VK_3);
        btnThree.addActionListener(e -> setInputNumber(3));
        btnFour.setMnemonic(KeyEvent.VK_4);
        btnFour.addActionListener(e -> setInputNumber(4));
        btnFive.setMnemonic(KeyEvent.VK_5);
        btnFive.addActionListener(e -> setInputNumber(5));
        btnSix.setMnemonic(KeyEvent.VK_6);
        btnSix.addActionListener(e -> setInputNumber(6));
        btnSeven.setMnemonic(KeyEvent.VK_7);
        btnSeven.addActionListener(e -> setInputNumber(7));
        btnEight.setMnemonic(KeyEvent.VK_8);
        btnEight.addActionListener(e -> setInputNumber(8));
        btnNine.setMnemonic(KeyEvent.VK_9);
        btnNine.addActionListener(e -> setInputNumber(9));

        ButtonGroup group = new ButtonGroup();
        group.add(btnZero);
        group.add(btnOne);
        group.add(btnTwo);
        group.add(btnThree);
        group.add(btnFour);
        group.add(btnFive);
        group.add(btnSix);
        group.add(btnSeven);
        group.add(btnEight);
        group.add(btnNine);

        numPanel.add(btnZero);
        numPanel.add(btnOne);
        numPanel.add(btnTwo);
        numPanel.add(btnThree);
        numPanel.add(btnFour);
        numPanel.add(btnFive);
        numPanel.add(btnSix);
        numPanel.add(btnSeven);
        numPanel.add(btnEight);
        numPanel.add(btnNine);

        add(numPanel, BorderLayout.SOUTH);
        add(new GridGuiOne(grid), BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public void setInputNumber(int n) {
        inputNumber = n;
        inputInc = true;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid.setCellValue(i, j, 0);
                grid.clearBG();
            }
        }
    }

    public void solve() {
        if(inputInc){
            BruteForce bf = new BruteForce(grid);
            bf.solve();
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid.getValue(i, j) == 0) {
                    grid.setCellValue(i, j, cellValue[i][j]);
                    grid.highlight(i, j);
                }
            }
        }

    }

    public void setDifficulty(int unFilled) {
        generator.resetGrid();
        inputInc = false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cellValue[i][j] = grid.getValue(i, j);
            }
        }
        int randX = 0;
        int randY = 0;
        int temp = 0;
        while (unFilled > 0) {
            randX = (int) (Math.random() * size);
            randY = (int) (Math.random() * size);
            if (grid.getValue(randX, randY) != 0) {
                temp = grid.getValue(randX, randY);
                grid.setCellValue(randX, randY, 0);
                status = BruteForce.isValidSudoku(grid);
                if (status) {
                    unFilled--;
                } else {
                    grid.setCellValue(randX, randY, temp);
                }
            }
        }
    }

    class Listener implements MouseListener {

        private int x, y;

        Listener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void mouseClicked(MouseEvent arg0) {
            if (cellValue[x][y] == 0) {
                grid.setCellValue(x, y, inputNumber);
                boolean status = BruteForce.isValidSudoku(grid);
                if (status) {
                } else {
                    grid.setCellValue(x, y, 0);
                }
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    //Sudoku main: Starts grid
    public static void main(String[] args) {
        Grid grid = new Grid();
        JFrame frame = new Sudoku(grid);
        frame.setVisible(true);
        frame.pack();
        frame.setBounds(100, 100, 800, 800);

    }
}

class Cell extends JLabel implements FocusListener, MouseListener, KeyListener {

    private static Font labelFont = new Font("Times New Roman", BOLD, 28);
    private static Color lightYellow = new Color(255, 255, 125);
    private String str;
    private int value;
    private Point point;
    private ArrayList<CellKbListener> listeners;

    public Cell(int x, int y) {
        super("     ");

        point = new Point(x, y);
        str = String.format("(%02d,%02d)", x, y);
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.RED));
        setFont(labelFont);
        setForeground(Color.RED);
        listeners = new ArrayList<>();
    }

    public int getValue() {
        return value;
    }

    public Point getPosition() {
        return point;
    }

    private void reset(int val) {
        value = val;
        if (value == 0) {
            setText("     ");
            return;
        }

        setText("  " + Integer.toString(value, Character.MAX_RADIX).toUpperCase() + "  ");
    }

    public void setValue(int value) {
        reset(value);
    }

    @Override
    public String toString() {
        return Integer.toString(value, Character.MAX_RADIX).toUpperCase();
    }

    public String getIdStr() {
        return str;
    }

    public boolean equals(Cell c) {
        return value == c.value;
    }

    @Override
    public void focusGained(FocusEvent e) {
        setBackground(lightYellow);
    }

    @Override
    public void focusLost(FocusEvent e) {
        setBackground(Color.WHITE);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void registerCellKbListener(CellKbListener listener) {
        if (listeners.isEmpty()) {
            setFocusable(true);
            addFocusListener(this);
            addMouseListener(this);
            addKeyListener(this);
            setFocusTraversalKeysEnabled(false);
        }
        listeners.add(listener);
    }

    @Override
    public void keyPressed(KeyEvent ev) {
        listeners.stream().forEach((listener) -> {
            listener.keyPressed(this, point.x, point.y, ev);
        });
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}

final class Grid implements CellKbListener {

    private int size;
    private Cell[][] cell;
    private RowColReg[] column, row, region;
    private int[][] regionId;

    public Grid() {
        this.size = 9;
        cell = new Cell[size][size];
        regionId = new int[size][size];
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                cell[row][col] = new Cell(row, col);
            }
        }
        row = new RowColReg[size];
        for (int i = 0; i < size; i++) {
            row[i] = new RowColReg(cell[i]);
        }

        column = new RowColReg[size];
        for (int i = 0; i < size; i++) {
            Cell[] c = new Cell[size];
            for (int j = 0; j < size; j++) {
                c[j] = cell[j][i];
            }
            column[i] = new RowColReg(c);
        }

        region = new RowColReg[size];
        int k = 0;
        Cell[] c;
        for (int i = 0; i < 3; i++) {
            int firstRow = i * 3;
            for (int j = 0; j < 3; j++) {
                int firstCol = j * 3;
                c = new Cell[size];
                int idx = 0;
                for (int rRow = 0; rRow < 3; rRow++) {
                    for (int rCol = 0; rCol < 3; rCol++) {
                        c[idx++] = cell[firstRow + rRow][firstCol + rCol];
                    }
                }
                region[k] = new RowColReg(c);
                for (Cell c1 : c) {
                    Point p = c1.getPosition();
                    regionId[p.x][p.y] = k;
                }
                k++;
            }
        }
    }

    public Grid(Grid g) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = g.getValue(i, j);
                if (value > 0) {
                    setCellValue(i, j, value);
                }
            }
        }
    }

    public int[] getPossibilities(int x, int y) {
        BitSet bs = new BitSet(size + 1);
        for (int i = 1; i <= size; i++) {
            if (isPossible(x, y, i)) {
                bs.set(i);
            }
        }
        int value = getValue(x, y);
        if (value > 0) {
            bs.set(value);
        }
        return buildArray(bs);
    }

    public void highlight(int x, int y) {
        cell[x][y].setBackground(Color.YELLOW);
    }

    public void clearBG() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell[i][j].setBackground(Color.BLACK);
            }
        }
    }

    public int[] getAvailableValues(Point p) {
        int x = p.x;
        int y = p.y;
        BitSet bs = new BitSet(size + 1);
        for (int i = 1; i <= size; i++) {
            if (isPossible(x, y, i)) {
                bs.set(i);
            }
        }
        int value = getValue(x, y);
        if (value > 0) {
            bs.set(value);
        }
        return buildArray(bs);
    }

    private int[] buildArray(BitSet bs) {
        int nb = bs.cardinality();
        if (nb == 0) {
            return new int[0];
        }
        int[] array = new int[nb];
        int k = 0;
        for (int i = 1; i <= size; i++) {
            if (bs.get(i)) {
                array[k++] = i;
            }
        }
        return array;
    }

    public boolean isPossible(int x, int y, int n) {
        if (!row[x].isPossible(n)) {
            return false;
        }
        if (!column[y].isPossible(n)) {
            return false;
        }
        return getRegion(x, y).isPossible(n);
    }

    private void validate(int x, int y, int value) {
        if (value == 0) {
            return;
        }
        if (value == getValue(x, y)) {
            return;
        }
        if (value > size || !isPossible(x, y, value)) {
            throw new IllegalStateException("Trying to set " + value + " at x: " + x + " y: " + y);
        }
    }

    public void setCellValue(int x, int y, int value) {
        validate(x, y, value);
        cell[x][y].setValue(value);
    }

    public RowColReg getRow(int x, int y) {
        return row[x];
    }

    public RowColReg getColumn(int x, int y) {
        return column[y];
    }

    public RowColReg getRegion(int x, int y) {
        return region[regionId[x][y]];
    }

    public int getSize() {
        return cell.length;
    }

    public Cell[][] getCells() {
        return cell;
    }

    public void clearCell() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cell[i][j].setValue(0);
            }
        }
    }

    int getValue(int x, int y) {
        return cell[x][y].getValue();
    }

    int getValue(Point p) {
        return getValue(p.x, p.y);
    }

    public ArrayList<Point> getCellsToFill() {
        ArrayList<Point> al = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cell[i][j].getValue() == 0) {
                    al.add(cell[i][j].getPosition());
                }
            }
        }
        return al;
    }

    public RowColReg[] getRegion() {
        return region;
    }

    public void registerForKeyboardEvent() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cell[i][j].registerCellKbListener(this);
            }
        }
    }

    @Override
    public void keyTyped(Cell cell, int x, int y, KeyEvent e) {
        int num = (int) e.getKeyChar();
        cell.setValue(num);
    }

    @Override
    public void keyPressed(Cell cell, int x, int y, KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_LEFT:
            case KeyEvent.VK_KP_RIGHT:
            case KeyEvent.VK_KP_UP:
            case KeyEvent.VK_KP_DOWN:
            case KeyEvent.VK_TAB:
                changeFocus(code, x, y);
                return;
            default:
                break;
        }
        char c = Character.toLowerCase(e.getKeyChar());
        try {
            int value = Integer.parseInt("" + c, Character.MAX_RADIX);
            if (value >= size) {
                return;
            }
            if (isPossible(x, y, value)) {
                cell.setValue(value);
                changeFocus(KeyEvent.VK_RIGHT, x, y);
            }
        } catch (Exception ex) {
        }
    }

    private void changeFocus(int code, int x, int y) {
        switch (code) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                y--;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
            case KeyEvent.VK_TAB:
                y++;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                x--;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                x++;
                break;
            default:
                return;
        }
        if (x < 0) {
            x = size - 1;
        }
        if (x >= size) {
            x = 0;
        }
        if (y < 0) {
            y = size - 1;
        }
        if (y >= size) {
            y = 0;
        }
        cell[x][y].requestFocus();
    }

    public boolean equals(Grid g) {
        if (size != g.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!(cell[i][j].equals(g.cell[i][j]))) {
                    return false;
                }
            }
        }
        return true;
    }
}

class BruteForce implements ActionListener {

    private Grid grid;
    private ArrayList<Point> al;
    private int nbTodo;
    private int index;
    private Timer timer;

    public BruteForce(Grid grid) {
        this.grid = grid;
    }

    public void solve() {
        solve(false);
    }

    public void solve(boolean reverse) {
        al = grid.getCellsToFill();
        nbTodo = al.size();
        if (nbTodo == 0) {
            return;
        }
        if (reverse) {
            al = reverse(al);
        }
        index = 0;
        while (index < nbTodo) {
            performFill();
        }
    }

    private void performFill() {
        if (index < 0) {
            throw new IllegalStateException("Trying to solve an invalid Sudoku problem");
        }
        Point p = al.get(index);
        int[] guess = grid.getPossibilities(p.x, p.y);
        if (guess.length == 0) {
            --index;
            return;
        }
        int value = grid.getValue(p);
        if (value == 0) {
            grid.setCellValue(p.x, p.y, guess[0]);
            ++index;
            return;
        }
        if (value == guess[guess.length - 1]) {
            grid.setCellValue(p.x, p.y, 0);
            --index;
            return;
        }
        for (int lastUsed = 0; lastUsed < guess.length; ++lastUsed) {
            if (value == guess[lastUsed]) {
                grid.setCellValue(p.x, p.y, guess[lastUsed + 1]);
                ++index;
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (index == nbTodo) {
            timer.stop();
            return;
        }
        performFill();
    }

    private ArrayList<Point> reverse(ArrayList<Point> order) {
        ArrayList<Point> rev = new ArrayList<>();
        int size = order.size();
        for (int i = size - 1; i >= 0; i--) {
            rev.add(order.get(i));
        }
        return rev;
    }

    public static boolean isValidSudoku(Grid origGrid) {
        Grid[] grid = new Grid[2];
        boolean[] reverseFlag = {false, true};
        for (int i = 0; i < 2; i++) {
            grid[i] = new Grid(origGrid);
            BruteForce bf = new BruteForce(grid[i]);
            bf.solve(reverseFlag[i]);
        }
        return grid[0].equals(grid[1]);
    }
}

class GridGuiOne extends JPanel {

    private Grid grid;

    GridGuiOne(Grid grid) {
        this.grid = grid;
        RowColReg[] region = grid.getRegion();
        int size = (int) Math.sqrt((double) region.length);
        setLayout(new GridLayout(size, size));
        for (RowColReg region1 : region) {
            JPanel p = new JPanel(new GridLayout(size, size));
            p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            Cell[] cell = region1.getCells();
            for (Cell cell1 : cell) {
                p.add(cell1);
            }
            add(p);
        }
    }

    public Grid getGrid() {
        return grid;
    }
}

class RowColReg {

    private Cell[] cell;

    public RowColReg(Cell[] array) {
        cell = array;
    }

    public boolean contains(int n) {
        for (Cell cell1 : cell) {
            if (cell1.getValue() == n) {
                return true;
            }
        }
        return false;
    }

    public boolean isPossible(int n) {
        if (n == 0) {
            return true;
        }
        return !contains(n);
    }

    @Override
    public String toString() {
        String str = "[" + cell[0].getIdStr();
        for (int i = 1; i < cell.length; i++) {
            str += " " + cell[i].getIdStr();
        }
        return str + "]";
    }

    Cell[] getCells() {
        return cell;
    }
}

interface CellKbListener {

    public void keyPressed(Cell cell, int x, int y, KeyEvent e);

    public void keyTyped(Cell cell, int x, int y, KeyEvent e);
}

final class SudokuGenerator {

    private Grid grid;
    private int size;
    private ArrayList<Point> al;
    private Random ran;
    private int stuckCounter;
    private int fallBack;
    private int nbIter;
    private int cellId;

    public SudokuGenerator(Grid grid) {
        this.grid = grid;
        init();
    }

    public SudokuGenerator(GridGuiOne gridPanel) {
        grid = gridPanel.getGrid();
        init();
    }

    private void init() {
        size = grid.getSize();
        al = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                al.add(new Point(i, j));
            }
        }
        ran = new Random();
    }

    public void resetGrid() {
        grid.clearCell();
        grid.clearBG();
        cellId = 0;
        stuckCounter = 0;
        fallBack = 5;
        nbIter = 0;

        resetConsole();
    }

    private void resetConsole() {
        while (cellId != size * size) {
            testNextOne();
        }
    }

    private void testNextOne() {
        if (nbIter > size * size * 3) {
            for (int i = 0; i < cellId; i++) {
                Point p = al.get(i);
                grid.setCellValue(p.x, p.y, 0);
            }
            nbIter = 0;
            stuckCounter = 0;
            fallBack = 5;
        }
        ++nbIter;
        if (stuckCounter > 5) {
            for (int i = 0; i < fallBack; i++) {
                Point p = al.get(cellId--);
                grid.setCellValue(p.x, p.y, 0);
            }
            stuckCounter = 0;
            ++fallBack;
            if (fallBack > 10) {
                fallBack = 5;
            }
        }
        Point p = al.get(cellId);
        int[] val = grid.getPossibilities(p.x, p.y);
        if (val.length == 0) {
            --cellId;
            Point previous = al.get(cellId);
            grid.setCellValue(previous.x, previous.y, 0);
            ++stuckCounter;
            return;
        }
        grid.setCellValue(p.x, p.y, val[ran.nextInt(val.length)]);
        for (int i = cellId + 1; i < size * size; i++) {
            Point tp = al.get(i);
            val = grid.getPossibilities(tp.x, tp.y);
            if (val.length == 0) {
                grid.setCellValue(p.x, p.y, 0);
                cellId -= 2;
                ++stuckCounter;
                break;
            }
        }
        cellId++;
    }
}
