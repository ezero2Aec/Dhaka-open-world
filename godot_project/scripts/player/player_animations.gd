// PlayerAnimations GDScript (3D adapted)
class_name PlayerAnimations
extends Node

@export var animated_sprite: AnimatedSprite3D

func update_animation(velocity: Vector3, is_sprinting: bool) -> void:
	if animated_sprite == null:
		return

	var speed = velocity.length()
	if speed > 10.0:
		if is_sprinting:
			animated_sprite.play("sprint")
		else:
			animated_sprite.play("walk")
		# Flip sprite based on X direction (scale.x negative for left)
		if velocity.x != 0:
			animated_sprite.scale.x = -1 if velocity.x < 0 else 1
	else:
		animated_sprite.play("idle")
