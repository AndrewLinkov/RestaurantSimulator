package restaurant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class RestaurantSimulation {

    // Потокобезопасный счетчик для генерации уникальных ID заказов
    private static final AtomicInteger orderIdGenerator = new AtomicInteger(1);
    // Счетчик успешно приготовленных блюд
    private static final AtomicInteger totalServed = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        // Ограниченная очередь: одновременно может ожидать не более 3 заказов
        BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>(3);

        // Создаем пул потоков для поваров (2 повара)
        ExecutorService cookPool = Executors.newFixedThreadPool(2);

        System.out.println("[Система] Ресторан открылся. Начинаем работу...\n");

        // Запускаем поваров работать в фоне
        for (int i = 1; i <= 2; i++) {
            cookPool.execute(new Cook(i, orderQueue));
        }

        // Запускаем поток клиентов (генератор заказов)
        Thread customerThread = new Thread(new CustomerGenerator(orderQueue));
        customerThread.start();

        // Даем симуляции поработать 10 секунд
        Thread.sleep(10000);

        // Плавно останавливаем симуляцию
        System.out.println("\n[Система] Ресторан закрывается. Прекращаем прием заказов...");
        customerThread.interrupt(); // Останавливаем генератор клиентов
        cookPool.shutdownNow();     // Принудительно закрываем пул поваров

        // Выводим финальную статистику
        System.out.println("\n[Итог] Всего приготовлено блюд: " + totalServed.get());
    }
}
