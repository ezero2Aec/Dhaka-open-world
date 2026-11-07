// PlayerModel GDScript (3D adapted)
class_name PlayerModel
extends CharacterBody3D

@export var walk_speed: float = 150.0
@export var sprint_speed: float = 300.0
@export var acceleration: float = 1200.0
@export var friction: float = 1000.0

# Expected child nodes
@onready var controls: PlayerControls = $PlayerControls
@onready var animations: PlayerAnimations = $PlayerAnimations
@onready var collisions: PlayerCollisions = $PlayerCollisions

var health: float = 100.0

func _ready() -> void:
	if collisions:
		collisions.hit_received.connect(_on_player_hit)

func _physics_process(delta: float) -> void:
	var move_dir: Vector3 = controls.get_movement_vector()
	var current_speed: float = sprint_speed if controls.is_sprinting else walk_speed
	var target_velocity: Vector3 = move_dir * current_speed

	# Apply acceleration & friction on the horizontal plane
	if move_dir != Vector3.ZERO:
		velocity = velocity.move_toward(target_velocity, acceleration * delta)
	else:
		velocity = velocity.move_toward(Vector3.ZERO, friction * delta)

	# Apply gravity (Godot automatically adds gravity to CharacterBody3D if enabled)
	# Ensure we keep vertical velocity from collisions
	# move_and_slide() handles sliding against colliders
	move_and_slide()

	# Update animation state based on velocity and sprint flag
	animations.update_animation(velocity, controls.is_sprinting)

func _on_player_hit(damage: float) -> void:
	health -= damage
	if health <= 0:
		die()

func die() -> void:
	set_physics_process(false)
	collisions.set_collision_active(false)
	if animations.has_node("animated_sprite"):
		var anim = animations.get_node("animated_sprite")
		if anim.has_method("play"):
			anim.play("death")
