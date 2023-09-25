import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetGrid extends Grid {

  private class Point {
    public final int x;
    public final int y;

    public Point(int _x, int _y) {
      this.x = _x;
      this.y = _y;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Point) {
        Point other = (Point) obj;
        return this.x == other.x && this.y == other.y;
      }
      return false;
    }

    @Override
    public int hashCode() {
      int prime = 31;
      int result = x;
      result = prime * result + y;
      return result;
    }
  }

  private HashSet<Point> field;

  public SetGrid(int _width, int _height, double prob) {
    super(_width, _height);
    Random r = new Random();
    field =
        Stream.generate(
                () -> {
                  return new Point(r.nextInt(this.width), r.nextInt(this.height));
                })
            .limit((int) ((double) this.width * (double) this.height * prob))
            .collect(Collectors.toCollection(HashSet::new));
  }

  @Override
  public CellState get_elem(int i, int j) {
    return this.field.contains(new Point(i, j)) ? CellState.ALIVE : CellState.DEAD;
  }

  @Override
  public void set_elem(int i, int j, CellState val) {
    switch (val) {
      case ALIVE:
        this.field.add(new Point(i, j));
        break;
      case DEAD:
        this.field.remove(new Point(i, j));
        break;
    }
  }

  private Stream<Point> moore_neighborhood(int i, int j) {
    List<Integer> offsets = Arrays.asList(-1, 0, 1);
    Stream<Point> product =
        offsets.stream().flatMap(a -> offsets.stream().flatMap(b -> Stream.of(new Point(a, b))));
    Stream<Point> filtered = product.filter(a -> !(a.x == 0 && a.y == 0));

    return filtered.map(a -> new Point(a.x + i, a.y + j));
  }

  @Override
  public void run_gol_step() {
    Stream<Point> valuable_points =
        this.field.stream()
            .flatMap(a -> moore_neighborhood(a.x, a.y))
            .filter(a -> !(a.x < 0 || a.y < 0 || a.x >= this.width || a.y >= this.height));

    Map<Point, Long> frequencies =
        valuable_points.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    Stream<Point> new_field_stream =
        frequencies.entrySet().stream()
            .filter(
                a ->
                    3 == a.getValue()
                        || (2 == a.getValue()
                            && this.get_elem(a.getKey().x, a.getKey().y) == CellState.ALIVE))
            .map(Map.Entry::getKey);

    this.field = new_field_stream.collect(Collectors.toCollection(HashSet::new));
  }
}
