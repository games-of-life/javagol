public abstract class Grid {
    protected int width;
    protected int height;

    public Grid(int _width, int _height) {
        width = _width;
        height = _height;
    }

    abstract CellState get_elem(int i, int j);
    abstract void set_elem(int i, int j, CellState val);
    abstract void run_gol_step();
}

