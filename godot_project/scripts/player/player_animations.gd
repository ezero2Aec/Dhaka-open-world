// PlayerAnimations GDScript (3D articulated model)
class_name PlayerAnimations
extends Node

# Expected hierarchy under the Player (CharacterBody3D):
#   Player
#     └─ Body (Node3D)
#         ├─ Head
#         ├─ Neck
#         ├─ Torso
#         ├─ UpperArmL
#         ├─ UpperArmR
#         ├─ LowerArmL
#         ├─ LowerArmR
#         ├─ HandL
#         ├─ HandR
#         ├─ UpperLegL
#         ├─ UpperLegR
#         ├─ LowerLegL
#         ├─ LowerLegR
#         ├─ ShoeL
#         └─ ShoeR

@onready var head: Node3D = $../Body/Head
@onready var neck: Node3D = $../Body/Neck
@onready var torso: Node3D = $../Body/Torso
@onready var upper_arm_l: Node3D = $../Body/UpperArmL
@onready var upper_arm_r: Node3D = $../Body/UpperArmR
@onready var lower_arm_l: Node3D = $../Body/LowerArmL
@onready var lower_arm_r: Node3D = $../Body/LowerArmR
@onready var hand_l: Node3D = $../Body/HandL
@onready var hand_r: Node3D = $../Body/HandR
@onready var upper_leg_l: Node3D = $../Body/UpperLegL
@onready var upper_leg_r: Node3D = $../Body/UpperLegR
@onready var lower_leg_l: Node3D = $../Body/LowerLegL
@onready var lower_leg_r: Node3D = $../Body/LowerLegR
@onready var shoe_l: Node3D = $../Body/ShoeL
@onready var shoe_r: Node3D = $../Body/ShoeR

var animation_time: float = 0.0
var idle_reset: bool = true

func update_animation(velocity: Vector3, is_sprinting: bool, on_floor: bool) -> void:
    animation_time += get_process_delta_time()
    var speed = velocity.length()
    var swing_speed = 8.0 if not is_sprinting else 12.0
    var swing = sin(animation_time * swing_speed) * 0.5

    if speed > 0.1 and on_floor:
        # Walking / running animation
        upper_arm_l.rotation.x = swing
        upper_arm_r.rotation.x = -swing
        lower_arm_l.rotation.x = swing * 0.5
        lower_arm_r.rotation.x = -swing * 0.5
        upper_leg_l.rotation.x = -swing
        upper_leg_r.rotation.x = swing
        lower_leg_l.rotation.x = -swing * 0.5
        lower_leg_r.rotation.x = swing * 0.5
        idle_reset = false
    elif not on_floor:
        # Jumping / falling animation
        upper_arm_l.rotation.x = -0.3
        upper_arm_r.rotation.x = -0.3
        lower_leg_l.rotation.x = 0.2
        lower_leg_r.rotation.x = 0.2
        idle_reset = false
    else:
        # Idle – reset limb rotations
        if not idle_reset:
            _reset_limbs()
            idle_reset = true

func _reset_limbs() -> void:
    upper_arm_l.rotation.x = 0
    upper_arm_r.rotation.x = 0
    lower_arm_l.rotation.x = 0
    lower_arm_r.rotation.x = 0
    upper_leg_l.rotation.x = 0
    upper_leg_r.rotation.x = 0
    lower_leg_l.rotation.x = 0
    lower_leg_r.rotation.x = 0
