package pizzeria.core;

import org.junit.jupiter.api.Test;
import pizzeria.logging.StateListener;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void setStateUpdatesState() {
        Order order = new Order(1, null);
        order.setState(OrderState.BEING_COOKED);
        assertEquals(OrderState.BEING_COOKED, order.getState());
    }

    @Test
    void setStateNotifiesListener() {
        Order[] captured = new Order[1];
        OrderState[] capturedState = new OrderState[1];

        StateListener listener = (o, s) -> {
            captured[0] = o;
            capturedState[0] = s;
        };

        Order order = new Order(42, listener);
        order.setState(OrderState.COOKED);

        assertSame(order, captured[0]);
        assertEquals(OrderState.COOKED, capturedState[0]);
    }

    @Test
    void setStateWithNullListenerDoesNotThrow() {
        Order order = new Order(1, null);
        assertDoesNotThrow(() -> order.setState(OrderState.DELIVERED));
    }

    @Test
    void toStringContainsIdAndState() {
        Order order = new Order(7, null);
        order.setState(OrderState.IN_STORAGE);
        String s = order.toString();
        assertTrue(s.contains("7"));
        assertTrue(s.contains("IN_STORAGE"));
    }
}
