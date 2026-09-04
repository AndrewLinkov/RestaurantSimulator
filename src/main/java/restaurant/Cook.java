package restaurant;

import java.util.concurrent.BlockingQueue;

public class Cook implements Runnable {
    private final int id;
    private final BlockingQueue<Order> queue;

    public Cook(int id, BlockingQueue<Order> queue) {
        this.id = id;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // take() блокирует поток, если очередь пуста, пока не появится новый заказ
                Order order = queue.take();

                System.out.printf("  [Повар %d] Взял в работу заказ №%d: %s\n", id, order.getId(), order.getName());

                // Симуляция готовки (занимает от 2 до 3 секунд)
                Thread.sleep(2000 + (int)(Math.random() * 1000));

                System.out.printf("  [Повар %d] Готов заказ №%d: %s! ✔\n", id, order.getId(), order.getName());
            }
        } catch (InterruptedException e) {
            // Нормальное завершение работы при остановке пула
        }
    }
}
