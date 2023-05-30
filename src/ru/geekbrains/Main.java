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
        fieldSizeWidth = 5;
        fieldSizeHeight = 5;
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
        int count = 0;
        // Проверка по горизонтали
        for (int j = 0; j < fieldSizeHeight; j++) {
            for (int i = 0; i < fieldSizeWidth; i++) {
                if (field[j][i] == c) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == WIN_COUNT) {
                    return true;
                }
            }
        }
        // Проверка по вертикали
        count = 0;
        for (int i = 0; i < fieldSizeWidth; i++) {
            for (int j = 0; j < fieldSizeHeight; j++) {
                if (field[j][i] == c) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == WIN_COUNT) {
                    return true;
                }
            }
        }
        /*
         * Проверка по диагонали
         * 
         * Запускаем цикл пока курсор в зоне поля или пока нет выигрышной комбинации
         * Запускаем цикл обхода поля и поиска значения, при этом обнуляем счетчик
         * выигрышной комбинации каждый раз когда переходим на другую строку поля
         * 
         * Далее проверяем выигрышную комбинацию и если есть выигрышная комбинация то
         * увеличиваем счетчик совпадения
         * 
         */
        int k = 0;
        while (k < fieldSizeHeight && k != WIN_COUNT) {
            for (int i = 0; i < fieldSizeHeight; i++) {
                count = 0;
                for (int j = 0; j < fieldSizeWidth; j++) {
                    if (i + j < fieldSizeHeight && (k + j < fieldSizeHeight) && field[k + j][i + j] == c) {
                        count++;
                    } else if (!(i - j < 0) && (k + j < fieldSizeHeight) && field[k + j][i - j] == c) {
                        count++;
                    }
                    if (count == WIN_COUNT) {
                        return true;
                    }
                }
            }
            k++;
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
     * @param с   крестик или нолик для определения победной комбинации
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
