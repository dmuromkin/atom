
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
public class GameTests {


    @Test
    public void game_Core_1() throws Exception {
        assertThat(Main.Game_Core("house","mouse"), is(equalTo(false)));
    }

    @Test
    public void game_Core_2() throws Exception {
        Main.Game_Core("goose","mouse");
        assertThat(Main.bulls, is(equalTo(3)) );
    }

    @Test
    public void game_Core_3() throws Exception {
        Main.Game_Core("close","mouse");
        assertThat(Main.bulls, is(equalTo(2)) );
        assertThat(Main.cows, is(equalTo(1)) );
    }
  }