// PlayerControls GDScript (3D adapted)
class_name PlayerControls
extends Node

# Signal emitted when sprint button state changes
signal sprint_toggled(is_sprinting: bool)

# Stored as a 2‑D vector for UI convenience; interpreted on X‑Z plane
var move_vector: Vector2 = Vector2.ZERO
var is_sprinting: bool = false

# Called by UI joystick (2‑D input)
func set_joystick_input(input_vector: Vector2) -> void:
	move_vector = input_vector.clamped(1.0)

# Called by UI sprint button
func set_sprint_button(pressed: bool) -> void:
	is_sprinting = pressed
	emit_signal("sprint_toggled", is_sprinting)

# Returns a 3‑D movement direction on the X‑Z plane.
func get_movement_vector() -> Vector3:
	# Keyboard fallback (ui_* actions)
	var dir2d = Vector2.ZERO
	dir2d.x = Input.get_action_strength("ui_right") - Input.get_action_strength("ui_left")
	dir2d.y = Input.get_action_strength("ui_down") - Input.get_action_strength("ui_up")

	if dir2d != Vector2.ZERO:
		return Vector3(dir2d.x, 0.0, dir2d.y).normalized()

	# Joystick fallback
	if move_vector != Vector2.ZERO:
		return Vector3(move_vector.x, 0.0, move_vector.y).normalized()

	return Vector3.ZERO
