class_name PlayerCollisions
extends Node

@export var body_collision_shape: CollisionShape2D
@export var hitbox_area: Area2D

signal hit_received(damage: float)

func _ready() -> void:
	if hitbox_area:
		hitbox_area.area_entered.connect(_on_hitbox_area_entered)

func set_collision_active(active: bool) -> void:
	if body_collision_shape:
		body_collision_shape.disabled = !active

func _on_hitbox_area_entered(area: Area2D) -> void:
	if area.is_in_group("hazards"):
		var damage = 10.0 # Default damage
		if area.has_method("get_damage"):
			damage = area.get_damage()
		emit_signal("hit_received", damage)

