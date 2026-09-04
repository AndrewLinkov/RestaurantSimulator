package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerGenerator implements Runnable  {

    private final BlockingQueue<Order> queue;
    private final String[] menu = {"Пицца", "Бургер", "Паста", "Суп", "Салат"};
    private static final AtomicInteger orderIdGenerator = new AtomicInteger(1);

    public CustomerGenerator(BlockingQueue<Order> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int id = orderIdGenerator.getAndIncrement();
                String dish = menu[(int) (Math.random() * menu.length)];
                Order order = new Order(id, dish);

                System.out.printf("[Клиент] Создан заказ №%d: %s. Ждет добавления...\n", id, dish);

                // put() блокирует поток, если очередь заполнена
                queue.put(order);
                System.out.printf("[Клиент] Заказ №%d отправлен на кухню.\n", id);

                // Клиенты приходят раз в 1-1.5 секунды
                Thread.sleep(1000 + (int)(Math.random() * 500));
            }
        } catch (InterruptedException e) {
            // Нормальное завершение работы при вызове interrupt()
        }
    }
}
