class_name PlayerControls
extends Node

# Signal sent when sprint button state changes
signal sprint_toggled(is_sprinting: bool)

var move_vector: Vector2 = Vector2.ZERO
var is_sprinting: bool = false

# Call this from UI Virtual Joystick controls
func set_joystick_input(input_vector: Vector2) -> void:
	move_vector = input_vector.clamped(1.0)

# Call this from UI Sprint Button
func set_sprint_button(pressed: bool) -> void:
	is_sprinting = pressed
	emit_signal("sprint_toggled", is_sprinting)

func get_movement_vector() -> Vector2:
	# Fallback to physical controls (keyboard/touch UI mapping)
	var dir = Vector2.ZERO
	dir.x = Input.get_action_strength("ui_right") - Input.get_action_strength("ui_left")
	dir.y = Input.get_action_strength("ui_down") - Input.get_action_strength("ui_up")
	
	if dir != Vector2.ZERO:
		return dir.normalized()
	return move_vector

