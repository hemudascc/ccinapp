ALTER TABLE `cc_inapp`.`tb_inapp_process_request` ADD COLUMN `advertiser_api_comment` TEXT NULL AFTER `response_object`;


#################

ALTER TABLE `cc_inapp`.`tb_inapp_tmt_config` ADD COLUMN `portal_url2` VARCHAR(255) NULL AFTER `portal_url`; 

#############
ALTER TABLE `cc_inapp`.`tb_campaign_details` ADD COLUMN `op_id` INT NULL AFTER `reg_date`; 
ALTER TABLE `cc_inapp`.`tb_service` ADD COLUMN `advertiser_id` INT NULL AFTER `service_id`; 
CREATE VIEW `vw_service` AS SELECT `ts`.`service_id` AS `service_id`, `ts`.`service_name` AS `service_name`, `ts`.`status` AS `service_status`, `top`.`operator_id` AS `operator_id`, `top`.`operator_name` AS `operator_name`, `ts`.`advertiser_id` AS `advertiser_id`, `ta`.`advertiser_name` AS `advertiser_name` FROM ((`tb_service` `ts` LEFT JOIN `tb_operators` `top` ON ((`ts`.`op_id` = `top`.`operator_id`))) LEFT JOIN `tb_advertiser` `ta` ON ((`ts`.`advertiser_id` = `ta`.`id`))); 