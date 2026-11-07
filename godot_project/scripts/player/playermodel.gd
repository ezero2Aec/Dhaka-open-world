// PlayerModel GDScript (3D adapted)
class_name PlayerModel
extends CharacterBody3D

@export var walk_speed: float = 150.0
@export var sprint_speed: float = 300.0
@export var acceleration: float = 1200.0
@export var friction: float = 1000.0
@export var jump_speed: float = 350.0

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
        velocity.x = velocity.move_toward(target_velocity, acceleration * delta).x
        velocity.z = velocity.move_toward(target_velocity, acceleration * delta).z
    else:
        velocity.x = velocity.move_toward(Vector3.ZERO, friction * delta).x
        velocity.z = velocity.move_toward(Vector3.ZERO, friction * delta).z

    # Apply gravity
    if not is_on_floor():
        velocity.y -= ProjectSettings.get_setting("physics/3d/default_gravity") * delta
    else:
        # Allow jumping when on floor
        if Input.is_action_just_pressed("jump"):
            velocity.y = jump_speed
        else:
            velocity.y = 0

    # Move and slide
    velocity = move_and_slide()

    # Update animation state based on velocity, sprint flag, and ground state
    animations.update_animation(velocity, controls.is_sprinting, is_on_floor())

func _on_player_hit(damage: float) -> void:
    health -= damage
    if health <= 0:
        die()

func die() -> void:
    set_physics_process(false)
    collisions.set_collision_active(false)
    # Optional: play death animation if defined in body
