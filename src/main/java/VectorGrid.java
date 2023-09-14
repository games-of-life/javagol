import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Random;

public class VectorGrid extends Grid {
    private ArrayList<CellState> field;

    public VectorGrid(int _width, int _height, double prob) {
        super(_width, _height);
        Random r = new Random();
        field = Stream
            .generate(() -> { return r.nextDouble() > prob ? CellState.DEAD : CellState.ALIVE; })
            .limit(width * height)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public CellState get_elem(int i, int j) {
        return field.get(i * height + j);
    }

    @Override
    public void set_elem(int i, int j, CellState val) {
        field.set(i * height + j, val);
    }

    private int calculate_neighbors(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                int nx = x + i;
                int ny = y + j;
                if (!((i == 0 && j == 0)
                      || nx < 0 || ny < 0
                      || nx >= width || ny >= height)
                    && get_elem(nx, ny) == CellState.ALIVE) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void run_gol_step() {
        ArrayList<CellState> new_field = new ArrayList<>(field);

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int neigh = calculate_neighbors(i, j);
                if (3 == neigh || (2 == neigh && CellState.ALIVE == get_elem(i, j))) {
                    new_field.set(i * height + j, CellState.ALIVE);
                } else {
                    new_field.set(i * height + j, CellState.DEAD);
                }
            }
        }

        field = new_field;
    }
}
