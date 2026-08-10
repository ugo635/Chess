package com.me.chess.engine.evaluator;

import com.me.chess.pieces.impl.Knight;
import com.me.chess.vectors.Point;

public class KnightEvaluator {
    public static float getValue(Knight knight) {
        Point pos = knight.getPosition();
        final int x = pos.x;
        final int y = pos.y;

        float addedValue = 0;

        if (y == (knight.START_RANK + knight.TWO)) addedValue += 0.5f;

        if (x == 3 || x == 6) addedValue += 0.5f;
        else if (x == 2 || x == 5 || x == 4 || x == 7) addedValue += 0.0125f;
        else addedValue -= 1f;

        return 3 + addedValue;
    }
}
