package ru.geekbrains;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int WIN_COUNT = 4;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '•';
    private static final Scanner SCANNER = new Scanner(System.in);
    private static char[][] field; // Двумерный массив хранит текущее состояние игрового поля
    private static final Random RANDOM = new Random();
    private static int fieldSizeWidth; // Размерность игровога поля
    private static int fieldSizeHeight; // Размерность игровога поля

    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!")) {
                    break;
                }
                aiTurn();
                printField();
                if (gameCheck(DOT_AI, "Выиграл компьютер!")) {
                    break;
                }
            }
            System.out.println("Жедаете сыграть еще раз? (Y - да)");
            if (!SCANNER.next().equalsIgnoreCase("Y")) {
                break;
            }
        }
    }

    /**
     * Инициализация игрового поля
     */
    private static void initialize() {
        // Установим размер игрового поля
        fieldSizeWidth = 4;
        fieldSizeHeight = 3;
        field = new char[fieldSizeHeight][fieldSizeWidth];
        for (int y = 0; y < fieldSizeHeight; y++) {
            for (int x = 0; x < fieldSizeWidth; x++) {
                // Проинициализируем все элементы массива DOT_EMPTY (признак пустого поля)
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    /**
     * Отрисовка игрового поля
     *
     */
    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeWidth * 2 + 1; i++) {
            System.out.print((i % 2 == 0) ? "—" : i / 2 + 1);
        }
        System.out.println();
        for (int i = 0; i < fieldSizeHeight; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < fieldSizeWidth; j++) {
                System.out.print(field[i][j] + "|");
            }
            System.out.println();
        }
        for (int i = 0; i < fieldSizeWidth * 2 + 2; i++) {
            System.out.print("—");
        }
        System.out.println();
    }

    /**
     * Обработка хода игрока (человека)
     */
    private static void humanTurn() {
        int x, y;
        do {
            System.out.print("введите координаты хода X и Y (от 1 до 3) через пробел >>> ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[y][x] = DOT_HUMAN;
    }

    /**
     * Проверка, ячейка является пустой
     * 
     * @param x координата по шерине поля
     * @param y координата по высоте поля
     * @return true ячейка пустая или false ячейка не пустая
     */
    static boolean isCellEmpty(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода (координаты хода не должны превышать размерность
     * массива, игрового поля)
     * 
     * @param x координата по шерине поля
     * @param y координата по высоте поля
     * @return true координата в размерности поля или false координата вне поля
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeWidth && y >= 0 && y < fieldSizeHeight;
    }

    /**
     * Ход компьютера
     */
    private static void aiTurn() {
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeWidth);
            y = RANDOM.nextInt(fieldSizeHeight);
        } while (!isCellEmpty(x, y));
        field[y][x] = DOT_AI;
    }

    /**
     * Проверка победы
     * 
     * @param c крестик или нолик для проверки хода
     * @return true если победа, false если нет выигрошной комбинации
     */
    static boolean checkWin(char c) {
        // Проверка по трем горизонталям
        if (field[0][0] == c && field[0][1] == c && field[0][2] == c) {
            return true;
        }
        if (field[1][0] == c && field[1][1] == c && field[1][2] == c) {
            return true;
        }
        if (field[2][0] == c && field[2][1] == c && field[2][2] == c) {
            return true;
        }
        // Проверка по трем вертикалям
        if (field[0][0] == c && field[1][0] == c && field[2][0] == c) {
            return true;
        }
        if (field[0][1] == c && field[1][1] == c && field[2][1] == c) {
            return true;
        }
        if (field[0][2] == c && field[1][2] == c && field[2][2] == c) {
            return true;
        }
        // Проверка по диоганалям
        if (field[0][0] == c && field[1][1] == c && field[2][2] == c) {
            return true;
        }
        if (field[0][2] == c && field[1][1] == c && field[2][0] == c) {
            return true;
        }
        return false;
    }

    /**
     * Проверка на ничью
     * 
     * @return true ничья, false игра продолжается
     */
    static boolean checkDraw() {
        for (int y = 0; y < fieldSizeHeight; y++) {
            for (int x = 0; x < fieldSizeWidth; x++) {
                if (isCellEmpty(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Метод проверки состояния игры
     * 
     * @param c   крестик или нолик для определения победной комбинации
     * @param str строка сообщения о том кто победил
     * @return true есть победитель, false победителя нет игра продолжается
     */
    static boolean gameCheck(char c, String str) {
        if (checkWin(c)) {
            System.out.println(str);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья");
            return true;
        }
        return false; // Игра продолжается
    }
}
