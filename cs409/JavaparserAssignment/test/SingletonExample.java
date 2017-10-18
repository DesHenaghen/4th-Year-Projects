public final class Singleton {
    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() {
        return INSTANCE;
    }
}

public final class HeyHo {
    private static final HeyHo INSTANCE = new HeyHo();


    public final class HeyHo2 {
        private static final HeyHo2 INSTANCE = new HeyHo2();

        private HeyHo2() {}

        public static HeyHo2 getInstance() {
            return INSTANCE;
        }
    }

    private HeyHo() {}

    public static HeyHo getInstance() {
        return INSTANCE;
    }
}

public final class HoHey {
    private static final HoHey INSTANCE = new HoHey();

    private HoHey() {}

    public static HoHey getInstance() {
        return INSTANCE;
    }
}

public final class LazySingleton {
    private static volatile LazySingleton instance = null;

    private LazySingleton() {}

    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized(Singleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}