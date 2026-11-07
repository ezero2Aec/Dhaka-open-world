class_name PlayerAnimations
extends Node

@export var animated_sprite: AnimatedSprite2D

func update_animation(velocity: Vector2, is_sprinting: bool) -> void:
	if animated_sprite == null:
		return
		
	var speed = velocity.length()
	
	if speed > 10.0:
		if is_sprinting:
			animated_sprite.play("sprint")
		else:
			animated_sprite.play("walk")
			
		# Orient sprite left/right based on movement direction
		if velocity.x != 0:
			animated_sprite.flip_h = velocity.x < 0
	else:
		animated_sprite.play("idle")

