import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VectorGrid extends Grid {
    private ArrayList<CellState> field;

    public VectorGrid(int _width, int _height, double prob) {
        super(_width, _height);
        Random r = new Random();
        field =
                Stream.generate(
                                () -> {
                                    return r.nextDouble() > prob ? CellState.DEAD : CellState.ALIVE;
                                })
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
        for (int i = x - 1; i <= x + 1; ++i) {
            for (int j = y - 1; j <= y + 1; ++j) {
                if (!((i == x && j == y) || i < 0 || j < 0 || i >= width || j >= height)
                        && get_elem(i, j) == CellState.ALIVE) {
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
