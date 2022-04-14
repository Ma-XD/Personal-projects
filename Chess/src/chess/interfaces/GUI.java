package chess.interfaces;

import chess.Result;
import chess.board.Board;
import chess.board.Cell;
import chess.board.Colour;
import chess.board.Position;
import chess.figures.Figure;
import chess.figures.FigureName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class GUI extends JFrame implements ChessUI {
    private final int SIZE = 8;
    private final int IMAGE_SIZE = 80;
    private final int PANEL_START = IMAGE_SIZE;
    private final int PANEL_SIZE = IMAGE_SIZE * SIZE;
    private Board board = new Board();
    private Position from;
    private Position to;
    private boolean rollBack;
    private boolean restart;
    private final String[] moves = new String[6];
    private final int LINE_HEIGHT = IMAGE_SIZE / moves.length;

    public GUI() {
        Arrays.fill(moves, "");
        initButtons();
        initBoardPanel();
        initGUI();
    }

    private void initGUI() {
        pack();
        setTitle("Chess board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(734, 837);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initButtons() {
        JButton restartButton = new JButton(new ImageIcon(getImagePath(Result.RESTART.toString())));
        restartButton.setSize(IMAGE_SIZE, IMAGE_SIZE);
        restartButton.setLocation(0, PANEL_SIZE + PANEL_START);
        restartButton.addActionListener(e -> restart = true);
        add(restartButton);

        JButton backButton = new JButton(new ImageIcon(getImagePath(Result.ROLLBACK.toString())));
        backButton.setSize(IMAGE_SIZE, IMAGE_SIZE);
        backButton.setLocation(IMAGE_SIZE, PANEL_SIZE + PANEL_START);
        backButton.addActionListener(e -> rollBack = true);
        add(backButton);
    }

    private int getPanelX(int col) {
        return col * IMAGE_SIZE + PANEL_START;
    }

    private int getPanelY(int row) {
        return row * IMAGE_SIZE + PANEL_START;
    }

    private void initBoardPanel() {
        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawHistory(g);

                g.drawRect(PANEL_START, PANEL_START, PANEL_SIZE, PANEL_SIZE);

                for (int col = 0; col < SIZE; col++) {
                    g.drawImage(
                            getImage(Character.toString(col + 'A')),
                            GUI.this.getPanelX(col), 0,
                            this
                    );
                }

                for (int row = 0; row < SIZE; row++) {
                    g.drawImage(
                            getImage(Integer.toString(SIZE - row)),
                            0, GUI.this.getPanelY(row),
                            this
                    );

                    for (int col = 0; col < SIZE; col++) {
                        drawCell(g, row, col);
                    }
                }

                drawPosition(g);
            }
        };

        panel.addMouseListener(new MyMouseAdapter());
        panel.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        add(panel);
    }


    private void drawHistory(Graphics g) {
        int start = PANEL_SIZE + IMAGE_SIZE;
        for (int i = 0; i < moves.length; i++) {
            g.drawString(moves[i], 2 * IMAGE_SIZE, (i + 1) * LINE_HEIGHT + start);
        }
    }

    private void drawCell(Graphics g, int row, int col) {
        Cell cell = board.getCell(row, col);
        Figure figure = cell.figure;

        if (cell.colour == Colour.BLACK) {
            g.fillRect(
                    getPanelX(col),
                    getPanelY(row),
                    IMAGE_SIZE,
                    IMAGE_SIZE
            );
        }

        if (figure.getName() != FigureName.EMPTY) {
            g.drawImage(
                    getImage(figure.getColour() + "_" + figure.getName()),
                    getPanelX(col),
                    getPanelY(row),
                    this
            );
        }
    }

    private Image getImage(String name) {
        String filename = getImagePath(name);
        ImageIcon icon = new ImageIcon(filename);

        return icon.getImage();
    }

    private String getImagePath(String name) {
        return "src/chess/images/" + name + ".png";
    }

    private void drawPosition(Graphics g) {
        if (from != null) {
            Graphics2D g2D = (Graphics2D) g;
            g2D.setStroke(new BasicStroke(3));
            g2D.setColor(Color.GREEN);
            g2D.drawRect(
                    getPanelX(from.col),
                    getPanelY(from.row),
                    IMAGE_SIZE,
                    IMAGE_SIZE
            );

            for (Position pos : board.getFigure(from).getLegalMoves()) {
                drawPoint(pos, g2D);
            }
        }
    }

    private void drawPoint(Position pos, Graphics2D g2D) {
        int pointRadius = 4;
        g2D.fillOval(
                getPanelX(pos.col) + IMAGE_SIZE / 2 - pointRadius,
                getPanelY(pos.row) + IMAGE_SIZE / 2 - pointRadius,
                pointRadius * 2,
                pointRadius * 2
        );
    }

    class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            int row = (e.getY() - PANEL_START) / IMAGE_SIZE;
            int col = (e.getX() - PANEL_START) / IMAGE_SIZE;

            if (e.getButton() == MouseEvent.BUTTON1 && board.isInside(row, col)) {
                setPos(new Position(row, col));
            }
        }

        private void setPos(Position position) {
            if (board.getTurn() == board.getFigure(position).getColour()) {
                from = position;
                repaint();
            } else if (from != null) {
                to = position;
            }
        }
    }


    @Override
    public void showBoard(Board board) {
        System.arraycopy(moves, 1, moves, 0, moves.length - 1);
        moves[moves.length - 1] = board.getStringMove();
        this.board = board.copy();
        repaint();
    }


    @Override
    public void whoseMove(Colour colour) {

    }

    @Override
    public void endMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public void invalidMove(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    FigureName selectFigure = FigureName.QUEEN;

    @Override
    public FigureName selectFigure(String pos) {
        JDialog selectDialog = new JDialog(this, "Select figure", true);
        selectDialog.setSize(4 * IMAGE_SIZE, IMAGE_SIZE);
        selectDialog.setLocationRelativeTo(null);

        Container container = selectDialog.getContentPane();
        container.setLayout(new GridLayout(1, 4));
        container.add(createButton(FigureName.QUEEN, "Queen", selectDialog));
        container.add(createButton(FigureName.ROOK, "Rook", selectDialog));
        container.add(createButton(FigureName.BISHOP, "Bishop", selectDialog));
        container.add(createButton(FigureName.KNIGHT, "Knight", selectDialog));

        selectDialog.setVisible(true);

        return selectFigure;
    }

    private JButton createButton(FigureName name, String text, JDialog dialog) {
        JButton button = new JButton(text);
        button.addActionListener(e -> {
            selectFigure = name;
            dialog.dispose();
        });

        return button;
    }

    @Override
    public Result testAction() {
        while (true) {

            if (from != null && to != null) {
                Result res = Result.UNKNOWN.setMessage(from.toString() + to.toString());
                from = null;
                to = null;
                repaint();
                return res;
            }

            if (rollBack) {
                rollBack = false;
                return Result.ROLLBACK.setMessage("1");
            }

            if (restart) {
                restart = false;
                return Result.RESTART;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }

}
