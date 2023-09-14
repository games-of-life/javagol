import static com.raylib.Jaylib.*;
import static com.raylib.Raylib.*;

public class Main {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int BOX_DIMENSION = 10;

    public static void main(String args[]) {
        InitWindow(WIDTH, HEIGHT, "Game of life");
        // SetTargetFPS(1);
        final int box_width = WIDTH / BOX_DIMENSION;
        final int box_height = HEIGHT / BOX_DIMENSION;

        Grid g = new VectorGrid(box_width, box_height, 0.5);

        while (!WindowShouldClose()) {
            BeginDrawing();
            ClearBackground(BLACK);
            for (int i = 0; i < box_width; ++i) {
                for (int j = 0; j < box_height; ++j) {
                    DrawRectangle(
                            BOX_DIMENSION * i,
                            BOX_DIMENSION * j,
                            BOX_DIMENSION - 1,
                            BOX_DIMENSION - 1,
                            g.get_elem(i, j) == CellState.ALIVE ? WHITE : BLACK);
                }
            }

            DrawFPS(10, 10);
            EndDrawing();

            g.run_gol_step();
            // break;
        }
        CloseWindow();
    }
}
