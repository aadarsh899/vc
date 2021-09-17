<?php
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: google/cloud/videointelligence/v1beta2/video_intelligence.proto

namespace GPBMetadata\Google\Cloud\Videointelligence\V1Beta2;

class VideoIntelligence
{
    public static $is_initialized = false;

    public static function initOnce() {
        $pool = \Google\Protobuf\Internal\DescriptorPool::getGeneratedPool();

        if (static::$is_initialized == true) {
          return;
        }
        \GPBMetadata\Google\Api\Annotations::initOnce();
        \GPBMetadata\Google\Api\Client::initOnce();
        \GPBMetadata\Google\Api\FieldBehavior::initOnce();
        \GPBMetadata\Google\Longrunning\Operations::initOnce();
        \GPBMetadata\Google\Protobuf\Duration::initOnce();
        \GPBMetadata\Google\Protobuf\Timestamp::initOnce();
        \GPBMetadata\Google\Rpc\Status::initOnce();
        $pool->internalAddGeneratedFile(
            '
�%
?google/cloud/videointelligence/v1beta2/video_intelligence.proto&google.cloud.videointelligence.v1beta2google/api/client.protogoogle/api/field_behavior.proto#google/longrunning/operations.protogoogle/protobuf/duration.protogoogle/protobuf/timestamp.protogoogle/rpc/status.proto"�
AnnotateVideoRequest
	input_uri (	
input_content (F
features (2/.google.cloud.videointelligence.v1beta2.FeatureB�AK
video_context (24.google.cloud.videointelligence.v1beta2.VideoContext

output_uri (	B�A
location_id (	B�A"�
VideoContextF
segments (24.google.cloud.videointelligence.v1beta2.VideoSegment\\
label_detection_config (2<.google.cloud.videointelligence.v1beta2.LabelDetectionConfigg
shot_change_detection_config (2A.google.cloud.videointelligence.v1beta2.ShotChangeDetectionConfigq
!explicit_content_detection_config (2F.google.cloud.videointelligence.v1beta2.ExplicitContentDetectionConfigZ
face_detection_config (2;.google.cloud.videointelligence.v1beta2.FaceDetectionConfig"�
LabelDetectionConfigX
label_detection_mode (2:.google.cloud.videointelligence.v1beta2.LabelDetectionMode
stationary_camera (
model (	"*
ShotChangeDetectionConfig
model (	"/
ExplicitContentDetectionConfig
model (	"D
FaceDetectionConfig
model (	
include_bounding_boxes ("x
VideoSegment4
start_time_offset (2.google.protobuf.Duration2
end_time_offset (2.google.protobuf.Duration"i
LabelSegmentE
segment (24.google.cloud.videointelligence.v1beta2.VideoSegment

confidence ("P

LabelFrame.
time_offset (2.google.protobuf.Duration

confidence ("G
Entity
	entity_id (	
description (	
language_code (	"�
LabelAnnotation>
entity (2..google.cloud.videointelligence.v1beta2.EntityI
category_entities (2..google.cloud.videointelligence.v1beta2.EntityF
segments (24.google.cloud.videointelligence.v1beta2.LabelSegmentB
frames (22.google.cloud.videointelligence.v1beta2.LabelFrame"�
ExplicitContentFrame.
time_offset (2.google.protobuf.DurationR
pornography_likelihood (22.google.cloud.videointelligence.v1beta2.Likelihood"i
ExplicitContentAnnotationL
frames (2<.google.cloud.videointelligence.v1beta2.ExplicitContentFrame"Q
NormalizedBoundingBox
left (
top (
right (
bottom ("T
FaceSegmentE
segment (24.google.cloud.videointelligence.v1beta2.VideoSegment"�
	FaceFrame`
normalized_bounding_boxes (2=.google.cloud.videointelligence.v1beta2.NormalizedBoundingBox.
time_offset (2.google.protobuf.Duration"�
FaceAnnotation
	thumbnail (E
segments (23.google.cloud.videointelligence.v1beta2.FaceSegmentA
frames (21.google.cloud.videointelligence.v1beta2.FaceFrame"�
VideoAnnotationResults
	input_uri (	Z
segment_label_annotations (27.google.cloud.videointelligence.v1beta2.LabelAnnotationW
shot_label_annotations (27.google.cloud.videointelligence.v1beta2.LabelAnnotationX
frame_label_annotations (27.google.cloud.videointelligence.v1beta2.LabelAnnotationP
face_annotations (26.google.cloud.videointelligence.v1beta2.FaceAnnotationN
shot_annotations (24.google.cloud.videointelligence.v1beta2.VideoSegment^
explicit_annotation (2A.google.cloud.videointelligence.v1beta2.ExplicitContentAnnotation!
error	 (2.google.rpc.Status"s
AnnotateVideoResponseZ
annotation_results (2>.google.cloud.videointelligence.v1beta2.VideoAnnotationResults"�
VideoAnnotationProgress
	input_uri (	
progress_percent (.

start_time (2.google.protobuf.Timestamp/
update_time (2.google.protobuf.Timestamp"u
AnnotateVideoProgress\\
annotation_progress (2?.google.cloud.videointelligence.v1beta2.VideoAnnotationProgress*�
Feature
FEATURE_UNSPECIFIED 
LABEL_DETECTION
SHOT_CHANGE_DETECTION
EXPLICIT_CONTENT_DETECTION
FACE_DETECTION*r
LabelDetectionMode$
 LABEL_DETECTION_MODE_UNSPECIFIED 
	SHOT_MODE

FRAME_MODE
SHOT_AND_FRAME_MODE*t

Likelihood
LIKELIHOOD_UNSPECIFIED 
VERY_UNLIKELY
UNLIKELY
POSSIBLE

LIKELY
VERY_LIKELY2�
VideoIntelligenceService�
AnnotateVideo<.google.cloud.videointelligence.v1beta2.AnnotateVideoRequest.google.longrunning.Operation"i���"/v1beta2/videos:annotate:*�Ainput_uri,features�A.
AnnotateVideoResponseAnnotateVideoProgressT�A videointelligence.googleapis.com�A.https://www.googleapis.com/auth/cloud-platformB�
*com.google.cloud.videointelligence.v1beta2BVideoIntelligenceServiceProtoPZWgoogle.golang.org/genproto/googleapis/cloud/videointelligence/v1beta2;videointelligence�&Google.Cloud.VideoIntelligence.V1Beta2�&Google\\Cloud\\VideoIntelligence\\V1beta2�)Google::Cloud::VideoIntelligence::V1beta2bproto3'
        , true);

        static::$is_initialized = true;
    }
}

