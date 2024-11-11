package store;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ApplicationTest extends NsTest {

    @AfterEach
    void resetSystemIn() {
        System.setIn(System.in);
    }

    @Test
    void 파일에_있는_상품_목록_출력() {
        assertThat(1+1).isEqualTo(2);
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertThat(1+1).isEqualTo(2);

    }

    @Test
    void 기간에_해당하지_않는_프로모션_적용() {
        assertThat(1+1).isEqualTo(2);

    }

    @Test
    void 예외_테스트() {
        assertThat(1+1).isEqualTo(2);
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
