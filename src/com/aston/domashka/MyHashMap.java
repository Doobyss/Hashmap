import java.util.Objects;

public class MyHashMap<K, V> {

    // Внутренний класс — элемент (узел) списка
    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next; // ссылка на следующий элемент в "цепочке" (для коллизий)

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16; // стандартный размер
    private Entry<K, V>[] table;                    // массив "бакетов"
    private int size = 0;                           // количество элементов

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    // Хеш-функция: берём hashCode и нормализуем по длине массива
    private int index(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    // Добавление/обновление значения
    public void put(K key, V value) {
        int index = index(key);
        Entry<K, V> current = table[index];

        // если бакет пуст — просто вставляем
        if (current == null) {
            table[index] = new Entry<>(key, value);
            size++;
            return;
        }

        // иначе ищем совпадение по ключу
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value; // обновляем значение
                return;
            }
            if (current.next == null) {
                current.next = new Entry<>(key, value); // добавляем в конец цепочки
                size++;
                return;
            }
            current = current.next;
        }
    }

    // Получение значения по ключу
    public V get(K key) {
        int index = index(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null; // если не найдено
    }

    // Удаление по ключу
    public void remove(K key) {
        int index = index(key);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (prev == null) {
                    table[index] = current.next; // удаляем первый элемент цепочки
                } else {
                    prev.next = current.next;    // пропускаем текущий элемент
                }
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    public int size() {
        return size;
    }

    // Для теста
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        System.out.println("B -> " + map.get("B")); // 2

        map.put("B", 99); // обновление
        System.out.println("B -> " + map.get("B")); // 99

        map.remove("A");
        System.out.println("A -> " + map.get("A")); // null

        System.out.println("Size: " + map.size()); // 2
    }
}
