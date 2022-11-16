package com.example.snakegame.game

data class Snake(
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Int = 60,
    var height: Int = 60,
    var direction: Direction = Direction.UP,
    var size: Int = 0,
)
