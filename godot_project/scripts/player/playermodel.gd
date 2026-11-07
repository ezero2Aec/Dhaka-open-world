class_name PlayerModel
extends CharacterBody2D

@export var walk_speed: float = 150.0
@export var sprint_speed: float = 300.0
@export var acceleration: float = 1200.0
@export var friction: float = 1000.0

@onready var controls: PlayerControls = $PlayerControls
@onready var animations: PlayerAnimations = $PlayerAnimations
@onready var collisions: PlayerCollisions = $PlayerCollisions

var health: float = 100.0

func _ready() -> void:
	if collisions:
		collisions.hit_received.connect(_on_player_hit)

func _physics_process(delta: float) -> void:
	var move_dir = controls.get_movement_vector()
	var current_speed = sprint_speed if controls.is_sprinting else walk_speed
	var target_velocity = move_dir * current_speed
	
	# Apply acceleration & friction
	if move_dir != Vector2.ZERO:
		velocity = velocity.move_toward(target_velocity, acceleration * delta)
	else:
		velocity = velocity.move_toward(Vector2.ZERO, friction * delta)
		
	move_and_slide()
	
	# Pass state to animations component
	animations.update_animation(velocity, controls.is_sprinting)

func _on_player_hit(damage: float) -> void:
	health -= damage
	if health <= 0:
		die()

func die() -> void:
	set_physics_process(false)
	collisions.set_collision_active(false)
	if animations.animated_sprite:
		animations.animated_sprite.play("death")

