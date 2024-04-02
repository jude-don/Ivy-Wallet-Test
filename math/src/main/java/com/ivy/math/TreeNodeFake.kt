package com.ivy.math

class TreeNodeFake(private val value: Double):TreeNode {
    override fun print(): String = value.toString()

    override fun eval(): Double = value
}